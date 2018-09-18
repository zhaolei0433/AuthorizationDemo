package cn.ipanel.authorization.demo.dto;

/**
 * @author zhaolei
 * createTime 2018年9月17日 下午4:16:44
 */
public class LoginWordTokenQueue {
    private String word;
    private String uuid;
    private String method;

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public LoginWordTokenQueue() {
    }
}
