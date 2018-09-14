package cn.ipanel.authorization.demo.global;

/**
 * @author luzh
 */
public class RequestParamErrorException extends Exception{

	private static final long serialVersionUID = 6029238338869481420L;

    public RequestParamErrorException(String message) {
		super(message);
	}
	
}
