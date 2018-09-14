package cn.ipanel.authorization.demo.dto;

/**
 * Created with Intellij IDEA.
 *
 * @author luzh
 * Create: 下午6:56 2018/1/29
 * Modified By:
 * Description:
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
