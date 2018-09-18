package cn.ipanel.authorization.demo.global;

/**
 * @author zhaolei
 * createTime 2018年9月17日 下午4:16:44
 */
public class SystemDefines {
    /**
     * 生成token基本信息
     */
    public static final String JWT_TOKEN_HEADER = "Authorization";
    public static final String JWT_HEADER_PREFIX = "Bearer ";
    public static final String JWT_SECRET = "iPanE1pA2sWo2dByZL";
    public static final Long JWT_EXPIRATION = 60 * 60 * 24 * 7L;
    /**
     * 管理员登录状态超时时间
     */
    public static final int MANAGER_LOGIN_ACTIVE_TIME_OUT = 10 * 60;
    /**
     * jwt token信息
     */
    //令牌无效
    public static final String INVALID_TOKEN_MSG = "invalid token";
    public static final int INVALID_TOKEN_CODE = 9001;
    //过期的令牌
    public static final String EXPIRED_TOKEN_MSG = "expired token";
    public static final int EXPIRED_TOKEN_CODE = 9002;
    //踢出
    public static final String LOGIN_KICK_OUT_MSG = "kick out";
    public static final int LOGIN_KICK_OUT_CODE = 9003;
    //登录已过期
    public static final String LOGIN_EXPIRED_MSG = "login expired";
    public static final int LOGIN_EXPIRED_CODE = 9004;
    /**
     * redis KEY
     */
    public static final String LOGIN_INFO_REDIS_KEY = "systemAuthLoginInfo";
    public static final String LOGIN_WORD_REDIS_KEY = "systemAuthLoginWord";
    /**
     * 登录状态
     */
    // 登录状态正常
    public static final Integer LOGIN_STATUS_OK = 0;
    // 异地登录
    public static final Integer LOGIN_STATUS_KICK_OUT = 1;
    // 登录状态过期
    public static final Integer LOGIN_STATUS_EXPIRED = 2;
    /**
     * 登录设备
     */
    // pc
    public static final Integer DEVICE_PC = 1;
    // app
    public static final Integer DEVICE_APP = 2;
    // stb
    public static final Integer DEVICE_STB = 3;
    /**
     * 登录用户类型
     */
    //管理员
    public static final Integer USER_TYPE_MANAGER = 1;
    //基本用户
    public static final Integer USER_TYPE_USER = 2;
}
