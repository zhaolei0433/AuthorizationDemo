package cn.ipanel.authorization.demo.jwt;

import cn.ipanel.authorization.demo.global.SystemDefines;
import cn.ipanel.authorization.demo.jwt.exception.JWTInvalidTokenException;
import cn.ipanel.authorization.demo.jwt.exception.JwtExpiredTokenException;
import io.jsonwebtoken.*;
/**
 * @author zhaolei
 * createTime 2018年9月13日 下午3:54:59
 */
public class RawAccessJwtToken implements JwtToken {

    private String token;
    private String secret;

    public RawAccessJwtToken(String token, String secret) {
        this.token = token;
        this.secret = secret;
    }
    /**
     * Title: getTokenValue
     * Description: 获取token的值
     * @author zhaolei
     * createTime 2017年9月29日 下午2:55:18
     */
    public String getTokenValue() throws Exception {
        return parseClaims().getSubject();
    }

    private Claims parseClaims() throws Exception {
        try {
            return Jwts.parser().setSigningKey(this.secret).parseClaimsJws(this.token).getBody();
        } catch (UnsupportedJwtException | MalformedJwtException | IllegalArgumentException
                | SignatureException ex) {
            throw new JWTInvalidTokenException(SystemDefines.INVALID_TOKEN_MSG, ex);
        } catch (ExpiredJwtException expiredEx) {
            throw new JwtExpiredTokenException(this, SystemDefines.EXPIRED_TOKEN_MSG, expiredEx);
        }
    }

    /**
     * 刷新token
     * @param expiration 过期时间
     * @return jwt
     * @throws Exception e
     */
    public String refreshToken(Long expiration) throws Exception {
        Claims claims = parseClaims();
        return JwtTokenUtil.refreshToken(claims, secret, expiration);
    }

    @Override
    public String getToken() {
        return token;
    }
}
