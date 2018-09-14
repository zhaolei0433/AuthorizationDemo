package cn.ipanel.authorization.demo.jwt.exception;

import cn.ipanel.authorization.demo.jwt.JwtToken;

/**
 * @author zhaolei
 * createTime 2018年9月13日 下午3:54:59
 */
public class JwtExpiredTokenException extends Exception {
    private static final long serialVersionUID = -5959543783324224864L;
    
    private JwtToken token;

    public JwtExpiredTokenException(String msg) {
        super(msg);
    }

    public JwtExpiredTokenException(JwtToken token, String msg, Throwable t) {
        super(msg, t);
        this.token = token;
    }

    public String token() {
        return this.token.getToken();
    }
}
