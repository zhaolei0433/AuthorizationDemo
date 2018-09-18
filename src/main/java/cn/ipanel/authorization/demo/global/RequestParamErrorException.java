package cn.ipanel.authorization.demo.global;

/**
 * @author zhaolei
 * createTime 2018年9月17日 下午4:16:44
 */
public class RequestParamErrorException extends Exception{

	private static final long serialVersionUID = 6029238338869481420L;

    public RequestParamErrorException(String message) {
		super(message);
	}
	
}
