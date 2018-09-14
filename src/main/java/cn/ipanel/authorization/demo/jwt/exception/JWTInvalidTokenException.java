package cn.ipanel.authorization.demo.jwt.exception;

/**
 * @author zhaolei
 * createTime 2018年9月13日 下午3:54:59
 */
public class JWTInvalidTokenException extends Exception {
	private static final long serialVersionUID = 4748208076537515594L;

	public JWTInvalidTokenException(String msg, Throwable t) {
		super(msg, t);
	}
	
	public JWTInvalidTokenException(String msg) {
	    super(msg);
	}
}
