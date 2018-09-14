package cn.ipanel.authorization.demo.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with Intellij IDEA.
 *
 * @author luzh
 * Create: 下午7:40 2018/1/25
 * Modified By:
 * Description:
 */
public class LoginWordAndToken {
    private String word;
    private List<String> uuid;

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public List<String> getUuid() {
        return uuid;
    }

    public void setUuid(List<String> uuid) {
        this.uuid = uuid;
    }

    public void addUuid(String uuid) {
        if (null == this.uuid) {
            this.uuid = new ArrayList<>(1);
        }
        this.uuid.add(uuid);
    }

    public void removeUuid(String uuid) {
        this.uuid.remove(uuid);
    }

    public LoginWordAndToken(String word, String uuid) {
        this.word = word;
        addUuid(uuid);
    }

    public LoginWordAndToken() {
    }
}
