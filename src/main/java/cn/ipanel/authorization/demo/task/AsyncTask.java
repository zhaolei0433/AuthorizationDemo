package cn.ipanel.authorization.demo.task;


import cn.ipanel.authorization.demo.dto.LoginInfo;
import cn.ipanel.authorization.demo.dto.LoginWordAndToken;
import cn.ipanel.authorization.demo.global.SystemDefines;
import cn.ipanel.authorization.demo.service.MyRedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author zhaolei
 * createTime 2018年9月17日 下午4:16:44
 */

@Component
public class AsyncTask {

    private static Logger logger = LoggerFactory.getLogger(AsyncTask.class);

    @Resource
    private MyRedisService redisService;

    @Resource
    private ConcurrentHashMap<String, Long> pcManagerActiveTime;
    /**
     * 单点登录，同一账号如果有新的登录操作，删除之前的登录信息。
     * @param word 管理员用户名或者普通用户手机号
     * @param device 设备类型
     * @param loginTime 登录时间
     */
    @Async("myAsync")
    public void cleanUserLoginOnDevice(String word, Integer device, Long loginTime) {
        LoginWordAndToken token = redisService.getLoginWord(word);
        List<String> tokens = new ArrayList<>();
        token.getUuid().forEach(uuid -> {
            LoginInfo info = redisService.getLoginInfo(uuid);
            if (null != info && info.getDeviceType().equals(device) && loginTime > info.getLoginTime()) {
                if (Instant.now().toEpochMilli() - info.getLoginTime() >= SystemDefines.JWT_EXPIRATION * 1000) {
                    logger.info("login info expired {} (jwt)", info.getWord());
                    redisService.removeLoginInfo(uuid);
                } else {
                    info.setStatus(SystemDefines.LOGIN_STATUS_KICK_OUT);
                    info.setLoseTime(Instant.now().toEpochMilli());
                    logger.info("login kickout {}, last login {}", info.getWord(), LocalDateTime.ofInstant(Instant.ofEpochMilli(info.getLoginTime()), ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                    redisService.saveLoginInfo(uuid, info);
                }
            } else if (null != info) {
                tokens.add(uuid);
            }
        });
        token.setUuid(tokens);
        redisService.saveLoginWord(word, tokens);
    }
    /**
     * 更新活动时间
     * @param username
     */
    @Async("myAsync")
    public void updateManagerPcActiveTime(String username) {
        try {
            pcManagerActiveTime.put(username, Instant.now().toEpochMilli());
        } catch (Exception e) {
            logger.error("updateManagerPcActiveTime: {}", e.getMessage(), e);
        }
    }

    /**
     * 系统启动初始化管理员登录活动时间
     */
    @Async("myAsync")
    public void initPcManagerLogin() {
        logger.info("system start init pcManagerLogin {}", LocalDateTime.now().toString());
        Map<String, LoginInfo> map = redisService.getAllLoginInfo();
        map.forEach((key, value) -> {
            if (Objects.equals(value.getDeviceType(), SystemDefines.DEVICE_PC) && Objects.equals(value.getUserType(), SystemDefines.USER_TYPE_MANAGER) && Objects.equals(value.getStatus(), SystemDefines.LOGIN_STATUS_OK)) {
                pcManagerActiveTime.put(value.getWord(), value.getLoginTime());
            }
        });
    }
}
