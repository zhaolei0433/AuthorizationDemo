package cn.ipanel.authorization.demo.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * Created with Intellij IDEA.
 *
 * @author luzh
 * Create: 下午3:29 2018/1/24
 * Modified By:
 * Description:
 */
@Data
public class LoginInfo implements Serializable{
    private static final long serialVersionUID = -8399145456817349715L;
    private Integer deviceType;
    private Integer userType;
    private String word;
    private Long loginTime;
    private String ip;
    private Long loseTime;
    private Integer status;

    public LoginInfo(Integer deviceType, Integer userType, String word, Long loginTime, String ip, Integer status) {
        this.deviceType = deviceType;
        this.userType = userType;
        this.word = word;
        this.loginTime = loginTime;
        this.ip = ip;
        this.status = status;
    }
}
