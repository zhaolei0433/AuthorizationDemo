package cn.ipanel.authorization.demo.controller.respones;

import cn.ipanel.authorization.demo.entity.ManagerInfo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author luzh
 * createTime 2017年9月28日 上午10:46:29
 */
@Data
public class ManagerVO {
    private Long id;
    private String username;
    private String password;
    private Long loginTime;
    private String token;
    public ManagerVO() {
    }

    public ManagerVO(ManagerInfo managerInfo, String token) {
        this.id = managerInfo.getId();
        this.username = managerInfo.getUserName();
        this.password = managerInfo.getPassword();
        this.loginTime = managerInfo.getLoginTime();
        this.token = token;
    }
}
