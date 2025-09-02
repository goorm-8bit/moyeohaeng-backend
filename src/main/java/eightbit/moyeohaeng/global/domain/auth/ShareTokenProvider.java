package eightbit.moyeohaeng.global.domain.auth;

import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;

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
public class ShareTokenProvider {

    // HS256 알고리즘에 필요한 최소 키 길이(바이트)
    private static final int MIN_KEY_LENGTH_BYTES = 32; // 256 bits

    // Share token custom claims
    private static final String CLAIM_IS_SHARE = "is_share";
    private static final String CLAIM_PROJECT_ID = "project_id";
    private static final String CLAIM_OWNER_EMAIL = "owner_email";
    private static final String CLAIM_USER_TYPE = "user_type"; // guest | viewer

    private final Key key;

    public ShareTokenProvider(@Value("${jwt.secret.guest}") String secretKey) {
        byte[] keyBytes;
        try {
            keyBytes = Base64.getDecoder().decode(secretKey);
        } catch (IllegalArgumentException e) {
            log.error("JWT 비밀키 Base64 디코딩 실패: {}", e.getMessage());
            throw new IllegalArgumentException("JWT 비밀키가 유효한 Base64 형식이 아닙니다.", e);
        }
        if (keyBytes.length < MIN_KEY_LENGTH_BYTES) {
            throw new IllegalArgumentException(
                "JWT 비밀키는 최소 " + MIN_KEY_LENGTH_BYTES + " 바이트(256비트) 이상이어야 합니다. 현재 키 길이: "
                    + keyBytes.length + " 바이트");
        }
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * 게스트/뷰어 공유 토큰 생성 (만료일 수 지정)
     */
    public String createShareToken(Long projectId, String ownerEmail, String userType, int expireDays) {
        Date now = new Date();
        long expireMillis = expireDays * 24L * 60L * 60L * 1000L;
        Date validity = new Date(now.getTime() + expireMillis);

        return Jwts.builder()
            .setSubject("share")
            .setIssuedAt(now)
            .setExpiration(validity)
            .setIssuer("moyeohaeng")
            .setId(UUID.randomUUID().toString())
            .claim(CLAIM_IS_SHARE, true)
            .claim(CLAIM_PROJECT_ID, projectId)
            .claim(CLAIM_OWNER_EMAIL, ownerEmail)
            .claim(CLAIM_USER_TYPE, userType)
            .signWith(key, SignatureAlgorithm.HS256)
            .compact();
    }

    public boolean validateShareToken(String token) {
        try {
            Claims claims = extractClaims(token);
            boolean isShare = Boolean.TRUE.equals(claims.get(CLAIM_IS_SHARE, Boolean.class));
            return isShare && !isTokenExpired(claims)
                && claims.get(CLAIM_PROJECT_ID) != null
                && claims.get(CLAIM_USER_TYPE) != null;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public Long getShareProjectId(String token) {
        Claims claims = extractClaims(token);
        Object v = claims.get(CLAIM_PROJECT_ID);
        if (v instanceof Number n)
            return n.longValue();
        if (v instanceof String s) {
            try {
                return Long.parseLong(s);
            } catch (NumberFormatException ignored) { }
        }
        throw new JwtException("유효하지 않은 project_id 클레임입니다.");
    }

    public String getShareOwnerEmail(String token) {
        Claims claims = extractClaims(token);
        String email = claims.get(CLAIM_OWNER_EMAIL, String.class);
        if (email == null || email.isBlank())
            throw new JwtException("owner_email 클레임이 없습니다.");
        return email;
    }

    public String getShareUserType(String token) {
        Claims claims = extractClaims(token);
        String type = claims.get(CLAIM_USER_TYPE, String.class);
        if (type == null || type.isBlank())
            throw new JwtException("user_type 클레임이 없습니다.");
        return type;
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

    private boolean isTokenExpired(Claims claims) {
        return claims.getExpiration().before(new Date());
    }
}
