package eightbit.moyeohaeng.global.domain.auth;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtTokenProvider {

	private final Key key;
	private final long accessTokenExpireLength;
	private final long refreshTokenExpireLength;

	public JwtTokenProvider(@Value("${jwt.secret.key}") String secretKey,
		@Value("${jwt.access-token.expire-length}") long accessTokenExpireLength,
		@Value("${jwt.refresh-token.expire-length}") long refreshTokenExpireLength) {
		this.key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
		this.accessTokenExpireLength = accessTokenExpireLength;
		this.refreshTokenExpireLength = refreshTokenExpireLength;
	}

	public String createAccessToken(String payload) {
		return createToken(payload, accessTokenExpireLength);
	}

	public String createRefreshToken(String payload) {
		return createToken(payload, refreshTokenExpireLength);
	}

	private String createToken(String payload, long expireLength) {
		Date now = new Date();
		Date validity = new Date(now.getTime() + expireLength);

		return Jwts.builder()
			.setSubject(payload)
			.setIssuedAt(now)
			.setExpiration(validity)
			.signWith(key, SignatureAlgorithm.HS256)
			.compact();
	}

	public String getPayload(String token) {
		try {
			return Jwts.parserBuilder()
				.setSigningKey(key)
				.build()
				.parseClaimsJws(token)
				.getBody()
				.getSubject();
		} catch (ExpiredJwtException e) {
			return e.getClaims().getSubject();
		} catch (JwtException e) {
			throw new JwtException("유효하지 않은 토큰입니다.");
		}
	}

	public boolean validateToken(String token) {
		try {
			Jwts.parserBuilder()
				.setSigningKey(key)
				.build()
				.parseClaimsJws(token);
			return true;
		} catch (JwtException | IllegalArgumentException e) {
			return false;
		}
	}
}