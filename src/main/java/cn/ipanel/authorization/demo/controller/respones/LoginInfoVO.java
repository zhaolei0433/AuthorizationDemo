package cn.ipanel.authorization.demo.controller.respones;

import cn.ipanel.authorization.demo.dto.LoginInfo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author zhaolei
 * createTime 2018年9月17日 下午4:16:44
 */
@Data
public class LoginInfoVO {
    private Integer userType;
    private String word;
    private Long loginTime;
    private String ip;

    public LoginInfoVO(LoginInfo info) {
        this.userType = info.getUserType();
        this.word = info.getWord();
        this.loginTime = info.getLoginTime();
        this.ip = info.getIp();
    }

}
