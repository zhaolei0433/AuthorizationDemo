package cn.ipanel.authorization.demo.global;

/**
 * @author zhaolei
 * createTime 2018年9月17日 下午4:16:44
 */
public class Globals {

    public static boolean isEmpty(Object str) {
        return (str == null || "".equals(str));
    }

    public static boolean isBlank(final CharSequence cs) {
        int strLen;
        if (cs == null || (strLen = cs.length()) == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if (!Character.isWhitespace(cs.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static Throwable getOriginException(Throwable e){
        while(e.getCause() != null){
            e = e.getCause();
        }
        return e;
    }

}