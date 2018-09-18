# AuthorizationDemo  授权验证
## 使用 Springboot+redis+jwt 实现授权验证基本功能框架
---
### 框架的功能与意义
1. 使用Springboot自带拦截器对请求进行拦截处理，这里使用springboot自带拦截器主要是较为轻型，同样也可以选择springsecurity。
2. 使用jwt作为创建验证token工具，并选择一种验证方式与算法。
3. 使用redis做缓存处理，缓存token等等登录信息，以实现单点登录与超时登录功能。
![image](http://note.youdao.com/noteshare?id=5877197d8df2ec59923f34176d823dea&sub=A06A1F107D844566921F0C13007D2DDC)
4. 使用springboot定时器配合redis实现超时登录，清除登录信息等功能。
5. 加rabbit消息队列控制单点与延时登录实现更为安全的登录状态控制（实现中）
---
### Springboot拦截器部分实现
1. 自定义拦截器实现HandlerInterceptor接口，可重写三个方法：preHandle，postHandle，afterCompletion，方法具体能实现功能请查看springboot拦截器。这里我们主要是在preHandle方法里，及在请求处理之前进行调用（Controller方法调用之前）来实现具体的业务逻辑。拦截器代码：

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
2. 使用jwt作为创建验证验证token的工具，生成token需要我们注意：自定义属性，过期时间，签名算法以及密匙。自定义属性可通过通UUID生成用唯一识别码，再加入特定内容等方式；在SignatureAlgorithm类中自带的签名算法有多种可任意选择，秘钥也是自己制定。生成token代码：
```
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






