package cn.ipanel.authorization.demo.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhaolei
 * createTime 2018年9月17日 下午4:16:44
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

    @Override
    public String toString() {
        return "LoginWordAndToken{" +
                "word='" + word + '\'' +
                ", uuid=" + uuid +
                '}';
    }
}
