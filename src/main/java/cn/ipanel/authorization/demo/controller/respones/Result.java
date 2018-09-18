package cn.ipanel.authorization.demo.controller.respones;

import java.io.Serializable;

/**
 * @author zhaolei
 * createTime 2018年9月17日 下午4:16:44
 */
public class Result<T> implements Serializable {

	private static final long serialVersionUID = 1L;
	private static final int SUCCESS_CODE = 0;
	private static final String SUCCESS_MSG = "success";
    private static final int FAILED_CODE = 1;
    private static final String FAILED_MSG = "failed";
	private String msg = SUCCESS_MSG;
	private int code = SUCCESS_CODE;
	private T data;

    public Result() {
		super();
	}

    public Result(T data, String msg, int code) {
		super();
		this.data = data;
		this.code = code;
		this.msg = msg;
	}

    public Result(String msg, int code) {
		super();
		this.code = code;
		this.msg = msg;
	}

    public Result(T data) {
		super();
		this.data = data;
		if (data instanceof Boolean) {
		    if (!(Boolean)data) {
                this.code = FAILED_CODE;
                this.msg = FAILED_MSG;
            }
        }
	}

    public String getMsg() {
		return msg;
	}

    public void setMsg(String msg) {
		this.msg = msg;
	}

    public int getCode() {
		return code;
	}

    public void setCode(int code) {
		this.code = code;
	}

    public T getData() {
		return data;
	}

    public void setData(T data) {
		this.data = data;
	}
}
