package cn.ipanel.authorization.demo.service.impl;

import cn.ipanel.authorization.demo.dto.LoginInfo;
import cn.ipanel.authorization.demo.dto.LoginWordAndToken;
import cn.ipanel.authorization.demo.global.SystemDefines;
import cn.ipanel.authorization.demo.service.MyRedisService;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created with Intellij IDEA.
 *
 * @author zhaolei
 * Create: 下午3:48 2018/1/24
 * Modified By:
 * Description:
 */
@Service
public class MyRedisServiceImpl implements MyRedisService {

    private static Logger logger = LoggerFactory.getLogger(MyRedisServiceImpl.class);
    private RedisTemplate<String, String> redisTemplate;
    private Gson gson = new Gson();

    @Autowired
    public MyRedisServiceImpl(RedisTemplate<String, String> redisTemplate) {
        Assert.notNull(redisTemplate, "RedisTemplate must not be null");
        this.redisTemplate = redisTemplate;
    }

    @Override
    public Boolean saveLoginInfo(String name, LoginInfo loginInfo) {
        try {
            redisTemplate.opsForHash().put(SystemDefines.LOGIN_INFO_REDIS_KEY, name, gson.toJson(loginInfo));
            return true;
        } catch (Exception e) {
            logger.error("save login error: {}", e.getMessage(), e);
            return false;
        }
    }

    @Override
    public LoginInfo getLoginInfo(String name) {
        return redisTemplate.opsForHash().hasKey(SystemDefines.LOGIN_INFO_REDIS_KEY, name)
                ? gson.fromJson(redisTemplate.opsForHash().get(SystemDefines.LOGIN_INFO_REDIS_KEY, name).toString(), LoginInfo.class)
                : null;
    }

    @Override
    public void removeLoginInfo(String name) {
        redisTemplate.opsForHash().delete(SystemDefines.LOGIN_INFO_REDIS_KEY, name);
    }

    @Override
    public Boolean saveLoginWord(String word, String uuid) {
        try {
            LoginWordAndToken wordAndToken = getLoginWord(word);
            if (null == wordAndToken) {
                wordAndToken = new LoginWordAndToken(word, uuid);
            } else {
                wordAndToken.addUuid(uuid);
            }
            redisTemplate.opsForHash().put(SystemDefines.LOGIN_WORD_REDIS_KEY, word, gson.toJson(wordAndToken));
            return true;
        } catch (Exception e) {
            logger.error("save login error: {}", e.getMessage(), e);
            return false;
        }
    }

    @Override
    public void saveLoginWord(String word, List<String> uuid) {
        LoginWordAndToken wordAndToken = new LoginWordAndToken();
        wordAndToken.setWord(word);
        wordAndToken.setUuid(uuid);
        redisTemplate.opsForHash().put(SystemDefines.LOGIN_WORD_REDIS_KEY, word, gson.toJson(wordAndToken));
    }

    @Override
    public LoginWordAndToken getLoginWord(String word) {
        return redisTemplate.opsForHash().hasKey(SystemDefines.LOGIN_WORD_REDIS_KEY, word)
                ? gson.fromJson(redisTemplate.opsForHash().get(SystemDefines.LOGIN_WORD_REDIS_KEY, word).toString(), LoginWordAndToken.class)
                : null;
    }

    @Override
    public Map<String, LoginInfo> getAllLoginInfo() {
        Map<String, LoginInfo> result = new HashMap<>(100);
        redisTemplate.opsForHash().entries(SystemDefines.LOGIN_INFO_REDIS_KEY).forEach((key, value) -> {
            result.put(key.toString(), gson.fromJson(value.toString(), LoginInfo.class));
        });
        return result;
    }

}
