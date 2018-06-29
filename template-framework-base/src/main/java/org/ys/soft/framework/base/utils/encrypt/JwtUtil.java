package org.ys.soft.framework.base.utils.encrypt;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.ys.soft.framework.base.exception.FrameworkException;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

/**
 * [JWT工具类]
 * 
 * @author Chris li[黎超]
 * @version [版本, 2017-04-12]
 * @see
 */
public class JwtUtil {
	/**
	 * 存放JWT头信息
	 */
	private static Map<String, Object> headMap = new HashMap<String, Object>();
	/**
	 * JWT签发者
	 */
	private static final String ISSUER = "ChrisLi";
	/**
	 * 加密密钥(仅存于服务器端)
	 */
	private static final String SECRET = "IlovEthiSgamE";

	static {
		headMap.put("typ", "JWT");
		headMap.put("alg", "HS256");
	}

	/**
	 * [创建JWT Token]
	 * 
	 * @author Chris li[黎超]
	 * @version [版本, 2017-04-12]
	 */
	public static String createToken(String claimKey, String claimValue, long activeTime) {
		try {
			Date expiresAt = new Date(System.currentTimeMillis() + activeTime);
			return JWT.create().withHeader(headMap).withIssuer(ISSUER).withClaim(claimKey, claimValue).withIssuedAt(new Date()).withExpiresAt(
					expiresAt).sign(Algorithm.HMAC256(SECRET));
		} catch (Exception e) {
			throw new FrameworkException(e.getLocalizedMessage());
		}
	}

	/**
	 * [验证JWT Token,并获取存放的信息]
	 * 
	 * @author Chris li[黎超]
	 * @version [版本, 2017-04-12]
	 */
	public static String verifyToken(String token, String claimKey) {
		try {
			JWTVerifier verifier = JWT.require(Algorithm.HMAC256(SECRET)).withIssuer(ISSUER).build();
			DecodedJWT jwt = verifier.verify(token);
			return jwt.getClaim(claimKey).asString();
		} catch (Exception e) {
			throw new FrameworkException(e.getLocalizedMessage());
		}
	}
}
