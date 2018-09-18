package cn.ipanel.authorization.demo.service;

import cn.ipanel.authorization.demo.controller.request.ManagerLoginReq;
import cn.ipanel.authorization.demo.controller.respones.LoginInfoVO;
import cn.ipanel.authorization.demo.controller.respones.ManagerVO;
import cn.ipanel.authorization.demo.entity.ManagerInfo;
import cn.ipanel.authorization.demo.global.MyException;

import java.util.List;

/**
 * @author zhaolei
 * createTime 2018年9月17日 下午4:16:44
 */
public interface ManagerService {
    /**
     * 通过用户名查询用户信息
     * @param userName
     * @return
     */
    List<ManagerInfo> queryUserInfo(String userName);
    /**
     * 管理员登录登录
     * @param managerLoginReq
     * @return
     */
    ManagerVO pcLogin(ManagerLoginReq managerLoginReq) throws Exception;
    /**
     * 用户url拦截，获取用户登录信息
     * @param token
     * @param device
     * @return
     * @throws Exception
     */
    LoginInfoVO getLoginInfo(String token, String device)throws Exception;
}
