## Springboot+redis+jwt 实现授权验证基本功能框架
---
### 框架的功能与意义
1. 使用Springboot自带拦截器对请求进行拦截处理，这里使用springboot自带拦截器主要是较为轻型，同样也可以选择springsecurity。
2. 使用jwt作为创建验证token工具，并选择一种验证方式与算法。
3. 使用redis做缓存处理，缓存token等等登录信息，以实现单点登录与超时登录功能。
4. 使用springboot定时器配合redis实现超时登录，清除登录信息等功能。
5. 加rabbit消息队列控制单点与延时登录实现更为安全的登录状态控制（实现中）
---
### Springboot拦截器实现部分
首先启动类@ServletComponentScan注解中开启拦截器配置，再自定义拦截器实现HandlerInterceptor接口，可重写三个方法：preHandle，postHandle，afterCompletion，方法具体能实现功能请查看springboot拦截器。这里我们主要是在preHandle方法里，及在请求处理之前进行调用（Controller方法调用之前）来实现具体的业务逻辑。
- 拦截器代码：

```
public class MyInterceptor implements HandlerInterceptor {
    private static Logger logger = LoggerFactory.getLogger(MyInterceptor.class);

    private ManagerService managerService = SpringUtil.getBean(ManagerService.class);
    private static final String TOKEN_HEADER = SystemDefines.JWT_TOKEN_HEADER;
    private static final String HEADER_PREFIX = SystemDefines.JWT_HEADER_PREFIX;

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        logger.info("token header == "+extract(httpServletRequest.getHeader(TOKEN_HEADER)));
        managerService.getLoginInfo(extract(httpServletRequest.getHeader(TOKEN_HEADER)), "pc");
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
      // System.out.println("postHandle被调用");
    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
       // System.out.println("afterCompletion被调用");
    }
    private static String extract(String header) throws Exception {
        if (Globals.isEmpty(header) || Globals.isBlank(header)) {
            throw new MyException("Authorization header cannot be blank");
        }
        if (!header.startsWith(HEADER_PREFIX) || header.length() <= HEADER_PREFIX.length()) {
            throw new MyException("Authorization header is error");
        }
        return header.substring(HEADER_PREFIX.length(), header.length());
    }
}
```

- 拦截器配置继承WebMvcConfigurationSupport类，重写addInterceptors与addResourceHandlers方法，在InterceptorRegistry 对象的addPathPatterns方法中添加需要拦截的url路径，在excludePathPatterns添加拦截放行的url路径。代码中拦截带/managerInfo/*路径的url，放行登录和swagger页面的url。

```
@Configuration
public class MyWebAppConfig extends WebMvcConfigurationSupport {
    @Bean
    MyInterceptor localInterceptor() {
        return new MyInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(localInterceptor())
                .addPathPatterns("/managerInfo/*")
                .excludePathPatterns("/managerInfo/login")//登录接口放行
                .excludePathPatterns("/swagger-resources/**", "/webjars/**", "/v2/**", "/swagger-ui.html/**");//swagger页面放行
    }

    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }
}
```
---
### jwt实现部分
pom文件引入引入jwt依赖，使用jwt作为创建验证验证token的工具，生成token需要我们注意：自定义属性，过期时间，签名算法以及密匙。自定义属性可通过通UUID生成用唯一识别码，再加入特定内容等方式；在SignatureAlgorithm类中自带的签名算法有多种可任意选择，秘钥也是自己制定。生成token代码：

```
/**
	 * 生成jwt token
	 * @param key 加密的内容
	 * @param signingKey 密钥
	 * @param expiration 过期时间
	 * @return jwt
	 */
	public static String generateToken(String key, String signingKey, Long expiration) {
		Map<String, Object> claims = new HashMap<>();
		claims.put(CLAIM_KEY_USERNAME, key);
		claims.put(CLAIM_KEY_CREATED, new Date());
		return generateToken(claims, signingKey, expiration);
	}

	private static String generateToken(Map<String, Object> claims, String singingKey, Long expiration) {
		return Jwts.builder()
				.setClaims(claims)                                                                        // 自定义属性
				.setExpiration(new Date(Instant.now().toEpochMilli() + expiration * 1000))                // 过期时间
				.signWith(SignatureAlgorithm.HS512, singingKey)                                           // 签名算法以及密匙
				.compact();
	}
```
---
### redis实现部分
pom文件引入引入redis依赖，配置文件配置redis参数。首先以service形式生成redis缓存工具类主要用于缓存以用户名与uuid为键值对的数据以及以uuid与logininfo为键值对的数据，代码较多这里不做详细贴出，请下载代码细看。

---
### 单点登录与限时未访问自动退出的实现部分
首先在启动类中@EnableScheduling注解开启定时器，@Async注解开启任务，@Scheduled()注解开启定时任务。代码如下：
- 单点登录主要实现部分
```
@Component
public class AsyncTask {

    private static Logger logger = LoggerFactory.getLogger(AsyncTask.class);

    @Resource
    private MyRedisService redisService;

    @Resource
    private ConcurrentHashMap<String, Long> pcManagerActiveTime;
    /**
     * 单点登录，同一账号如果有新的登录操作，删除之前的登录信息。
     * @param word 管理员用户名或者普通用户手机号
     * @param device 设备类型
     * @param loginTime 登录时间
     */
    @Async("myAsync")
    public void cleanUserLoginOnDevice(String word, Integer device, Long loginTime) {
        LoginWordAndToken token = redisService.getLoginWord(word);
        List<String> tokens = new ArrayList<>();
        token.getUuid().forEach(uuid -> {
            LoginInfo info = redisService.getLoginInfo(uuid);
            if (null != info && info.getDeviceType().equals(device) && loginTime > info.getLoginTime()) {
                if (Instant.now().toEpochMilli() - info.getLoginTime() >= SystemDefines.JWT_EXPIRATION * 1000) {
                    logger.info("login info expired {} (jwt)", info.getWord());
                    redisService.removeLoginInfo(uuid);
                } else {
                    info.setStatus(SystemDefines.LOGIN_STATUS_KICK_OUT);
                    info.setLoseTime(Instant.now().toEpochMilli());
                    logger.info("login kickout {}, last login {}", info.getWord(), LocalDateTime.ofInstant(Instant.ofEpochMilli(info.getLoginTime()), ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                    redisService.saveLoginInfo(uuid, info);
                }
            } else if (null != info) {
                tokens.add(uuid);
            }
        });
        token.setUuid(tokens);
        redisService.saveLoginWord(word, tokens);
    }
    /**
     * 更新活动时间
     * @param username
     */
    @Async("myAsync")
    public void updateManagerPcActiveTime(String username) {
        try {
            pcManagerActiveTime.put(username, Instant.now().toEpochMilli());
        } catch (Exception e) {
            logger.error("updateManagerPcActiveTime: {}", e.getMessage(), e);
        }
    }

    /**
     * 系统启动初始化管理员登录活动时间
     */
    @Async("myAsync")
    public void initPcManagerLogin() {
        logger.info("system start init pcManagerLogin {}", LocalDateTime.now().toString());
        Map<String, LoginInfo> map = redisService.getAllLoginInfo();
        map.forEach((key, value) -> {
            if (Objects.equals(value.getDeviceType(), SystemDefines.DEVICE_PC) && Objects.equals(value.getUserType(), SystemDefines.USER_TYPE_MANAGER) && Objects.equals(value.getStatus(), SystemDefines.LOGIN_STATUS_OK)) {
                pcManagerActiveTime.put(value.getWord(), value.getLoginTime());
            }
        });
    }
}
```
- 限时登录主要实现部分

```
@Component
public class ScheduleTask {
    
    private static Logger logger = LoggerFactory.getLogger(ScheduleTask.class);

    @Resource
    private MyRedisService myRedisService;

    @Resource
    private ConcurrentHashMap<String, Long> pcManagerActiveTime;

    
    /**
     * 每5分钟检测一次管理员pc端登录情况
     */
    @Scheduled(cron = "0 */5 * * * *")
    public void checkManagerActiveStatus() {
        logger.info("定时检测pc端管理员登录 {} ", LocalDateTime.now().toString());
        pcManagerActiveTime.forEach((username, activeTime) ->{
            if (Instant.now().toEpochMilli() - activeTime > SystemDefines.MANAGER_LOGIN_ACTIVE_TIME_OUT * 1000) {
                LoginWordAndToken token = myRedisService.getLoginWord(username);
                token.getUuid().forEach(uuid -> {
                    LoginInfo info = myRedisService.getLoginInfo(uuid);
                    if (null != info && info.getDeviceType().equals(SystemDefines.DEVICE_PC)) {
                        logger.info("设置为 expired {}, {}", username, uuid);
                        info.setStatus(SystemDefines.LOGIN_STATUS_EXPIRED);
                        info.setLoseTime(Instant.now().toEpochMilli());
                        myRedisService.saveLoginInfo(uuid, info);
                    }
                });
            }
        });
    }

    /**
     * 每30分钟清除失效登录信息
     */
    @Scheduled(cron = "0 */30 * * * *")
    public void clearLostLogin() {
        logger.info("定时清除失效登录信息 {}", LocalDateTime.now().toString());
        Map<String, LoginInfo> map = myRedisService.getAllLoginInfo();
        map.forEach((key, value) -> {
            // 清除过期1小时的登录信息
            if (null != value && null != value.getStatus() && SystemDefines.LOGIN_STATUS_OK != value.getStatus() && Instant.now().toEpochMilli() - value.getLoseTime() > 60 * 60 * 100) {
                myRedisService.removeLoginInfo(key);
            }
        });
    }
}
```
### 框架流程图

### 总结
撸代码之前问过一些前辈，在做项目的开发时授权验证方面，使用Springsecurity框架与Springboot自带的过滤器有什么优劣？用springboot+redis+jwt同样可实现简单的授权管理，与Springsecurity比较是否太过简单？得到如下回答：Springsecurity 是专门针对安全访问控制框架，过滤器是一种技术，就好比如servlet或者jsp。实现安全访问控制的方式有很多实现方式，但是用哪种方式需要结合自身的应用场景来判断，100万用户的程序设计与500万用户的程序设计肯定是不一样的。对于不同的项目采用不同的技术。后面有时间还会出几篇关于Springsecurity安全授权验证的demo，希望有不足的地方大家多多指点。
