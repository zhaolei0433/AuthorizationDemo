package cn.ipanel.authorization.demo.jwt;

/**
 * @author zhaolei
 * createTime 2018年9月13日 下午3:54:59
 */
import cn.ipanel.authorization.demo.global.SystemDefines;
import lombok.Data;

@Data
public class JwtSettings {

	private String jwTokenHeader = SystemDefines.JWT_TOKEN_HEADER;

	private String jwtTokenHead = SystemDefines.JWT_HEADER_PREFIX;

	private String jwtSecret = SystemDefines.JWT_SECRET;

	private Long jwtExpiration = SystemDefines.JWT_EXPIRATION;

}
