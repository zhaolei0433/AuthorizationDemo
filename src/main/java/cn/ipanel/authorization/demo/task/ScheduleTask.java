package cn.ipanel.authorization.demo.task;

import cn.ipanel.authorization.demo.dto.LoginInfo;
import cn.ipanel.authorization.demo.dto.LoginWordAndToken;
import cn.ipanel.authorization.demo.global.SystemDefines;
import cn.ipanel.authorization.demo.service.MyRedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author zhaolei
 * createTime 2018年9月17日 下午4:16:44
 */
@Component
public class ScheduleTask {
    
    private static Logger logger = LoggerFactory.getLogger(ScheduleTask.class);

    @Resource
    private MyRedisService myRedisService;

    @Resource
    private ConcurrentHashMap<String, Long> pcManagerActiveTime;

    
    /**
     * 每5分钟检测一次管理员pc端登录情况
     */
    @Scheduled(cron = "0 */5 * * * *")
    public void checkManagerActiveStatus() {
        logger.info("定时检测pc端管理员登录 {} ", LocalDateTime.now().toString());
        pcManagerActiveTime.forEach((username, activeTime) ->{
            if (Instant.now().toEpochMilli() - activeTime > SystemDefines.MANAGER_LOGIN_ACTIVE_TIME_OUT * 1000) {
                LoginWordAndToken token = myRedisService.getLoginWord(username);
                token.getUuid().forEach(uuid -> {
                    LoginInfo info = myRedisService.getLoginInfo(uuid);
                    if (null != info && info.getDeviceType().equals(SystemDefines.DEVICE_PC)) {
                        logger.info("设置为 expired {}, {}", username, uuid);
                        info.setStatus(SystemDefines.LOGIN_STATUS_EXPIRED);
                        info.setLoseTime(Instant.now().toEpochMilli());
                        myRedisService.saveLoginInfo(uuid, info);
                    }
                });
            }
        });
    }

    /**
     * 每30分钟清除失效登录信息
     */
    @Scheduled(cron = "0 */30 * * * *")
    public void clearLostLogin() {
        logger.info("定时清除失效登录信息 {}", LocalDateTime.now().toString());
        Map<String, LoginInfo> map = myRedisService.getAllLoginInfo();
        map.forEach((key, value) -> {
            // 清除过期1小时的登录信息
            if (null != value && null != value.getStatus() && SystemDefines.LOGIN_STATUS_OK != value.getStatus() && Instant.now().toEpochMilli() - value.getLoseTime() > 60 * 60 * 100) {
                myRedisService.removeLoginInfo(key);
            }
        });
    }
}
