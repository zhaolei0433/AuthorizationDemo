package cn.ipanel.authorization.demo.service;

import cn.ipanel.authorization.demo.dto.LoginInfo;
import cn.ipanel.authorization.demo.dto.LoginWordAndToken;
import java.util.List;

/**
 * Created with Intellij IDEA.
 *
 * @author zhaolei
 * Create: 下午3:47 2018/1/24
 * Modified By:
 * Description:
 */
public interface MyRedisService {

    /**
     * 保存登录信息
     * @param name uuid
     * @param loginInfo 登录信息
     * @return boolean
     */
    Boolean saveLoginInfo(String name, LoginInfo loginInfo);

    /**
     * 通过uuid获取登录信息
     * @param name uuid
     * @return info
     */
    LoginInfo getLoginInfo(String name);

    /**
     * 删除登录信息
     * @param name
     */
    void removeLoginInfo(String name);

    /**
     * 保存用户对应登陆信息
     * @param word word
     * @param uuid uuid
     * @return boolean
     */
    Boolean saveLoginWord(String word, String uuid);

    /**
     * 保存用户对应登陆信息
     * @param word word
     * @param uuid uuid
     */
    void saveLoginWord(String word, List<String> uuid);

    /**
     * 查找用户对应登录信息
     * @param word word
     * @return info
     */
    LoginWordAndToken getLoginWord(String word);



}
