package cn.ipanel.authorization.demo.jwt;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author zhaolei
 * createTime 2018年9月13日 下午3:54:59
 */
public class JwtTokenUtil{
	private static final String CLAIM_KEY_USERNAME = "sub";
	private static final String CLAIM_KEY_CREATED = "created";

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

	/**
	 * 刷新jwt
	 * @param claims claims
	 * @param singingKey 密钥
	 * @param expiration 过期时间
	 * @return 刷新后的jwt
	 */
	static String refreshToken(Claims claims, String singingKey, Long expiration) {
		claims.put(CLAIM_KEY_CREATED, new Date());
		return generateToken(claims, singingKey, expiration);
	}
}
