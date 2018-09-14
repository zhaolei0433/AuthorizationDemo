package cn.ipanel.authorization.demo.service.impl;

import lombok.Data;

/**
 * Created with Intellij IDEA.
 *
 * @author zhaolei
 * Create: 下午1:38 2018/2/7
 * Modified By:
 * Description:
 */
@Data
public class TokenAndUuidBean {
    private String token;
    private String uuid;

    public TokenAndUuidBean() {
    }

    public TokenAndUuidBean(String token, String uuid) {
        this.token = token;
        this.uuid = uuid;
    }
}
