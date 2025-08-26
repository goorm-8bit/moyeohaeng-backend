package eightbit.moyeohaeng.global.domain.auth;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class JwtTokenProvider {

	public enum TokenType {
		ACCESS, REFRESH
	}

	private static final String TOKEN_TYPE_KEY = "token_type";

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

	public String createAccessToken(String memberId) {
		return createToken(memberId, accessTokenExpireLength, TokenType.ACCESS);
	}

	public String createRefreshToken(String memberId) {
		return createToken(memberId, refreshTokenExpireLength, TokenType.REFRESH);
	}

	public String reissueAccessToken(String refreshToken) {
		if (!validateRefreshToken(refreshToken)) {
			throw new JwtException("유효하지 않은 리프레시 토큰입니다.");
		}

		String memberId = getMemberId(refreshToken);
		log.info("Refresh token received: {}", memberId);

		return createToken(memberId, accessTokenExpireLength, TokenType.ACCESS);
	}

	private String createToken(String subject, long expireLength, TokenType tokenType) {
		Date now = new Date();
		Date validity = new Date(now.getTime() + expireLength);

		return Jwts.builder()
			.setSubject(subject)
			.setIssuedAt(now)
			.setExpiration(validity)
			.claim(TOKEN_TYPE_KEY, tokenType.name())
			.signWith(key, SignatureAlgorithm.HS256)
			.compact();
	}

	public String getMemberId(String token) {
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

	private TokenType getTokenType(Claims claims) {
		try {
			return TokenType.valueOf(claims.get(TOKEN_TYPE_KEY, String.class));
		} catch (IllegalArgumentException e) {
			throw new JwtException("토큰 타입이 유효하지 않습니다.");
		}
	}
	
	private Claims extractClaims(String token) {
		try {
			return Jwts.parserBuilder()
				.setSigningKey(key)
				.build()
				.parseClaimsJws(token)
				.getBody();
		} catch (ExpiredJwtException e) {
			return e.getClaims();
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

	public boolean validateAccessToken(String token) {
		try {
			Claims claims = extractClaims(token);
			return getTokenType(claims) == TokenType.ACCESS && !bIsTokenExpired(claims);
		} catch (JwtException | IllegalArgumentException e) {
			return false;
		}
	}

	public boolean validateRefreshToken(String token) {
		try {
			Claims claims = extractClaims(token);
			return getTokenType(claims) == TokenType.REFRESH && !bIsTokenExpired(claims);
		} catch (JwtException | IllegalArgumentException e) {
			return false;
		}
	}

	private boolean bIsTokenExpired(Claims claims) {
		return claims.getExpiration().before(new Date());
	}
}