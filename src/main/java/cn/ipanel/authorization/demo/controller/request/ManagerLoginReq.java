package cn.ipanel.authorization.demo.controller.request;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author zhaolei
 * Create: 2018/9/12 15:53
 * Modified By:
 * Description:
 */
@Data
public class ManagerLoginReq {

    @ApiModelProperty(value = "登录名")
    private String username;
    @ApiModelProperty(value = "密码")
    private String password;
    @ApiModelProperty(value = "ip")
    private String ip;

    public ManagerLoginReq() {
    }

}
