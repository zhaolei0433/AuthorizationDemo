package cn.ipanel.authorization.demo.controller.respones;

import cn.ipanel.authorization.demo.dto.LoginInfo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created with Intellij IDEA.
 *
 * @author luzh
 * Create: 下午3:12 2018/1/24
 * Modified By:
 * Description:
 */
@Data
public class LoginInfoVO {
    @ApiModelProperty(value = "登录用户类型1管理员2普通用户")
    private Integer userType;
    @ApiModelProperty(value = "登录口令")
    private String word;
    @ApiModelProperty(value = "登录时间")
    private Long loginTime;
    @ApiModelProperty(value = "登录ip")
    private String ip;

    public LoginInfoVO(LoginInfo info) {
        this.userType = info.getUserType();
        this.word = info.getWord();
        this.loginTime = info.getLoginTime();
        this.ip = info.getIp();
    }

}
