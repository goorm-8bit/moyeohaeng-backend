package eightbit.moyeohaeng.global.config.security;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import eightbit.moyeohaeng.global.domain.auth.JwtTokenProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;

/**
 * JWT 토큰의 유효성을 검증하고 인증 정보를 설정하는 필터
 */
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String TOKEN_PREFIX = "Bearer ";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // 요청 헤더에서 JWT 토큰 추출
        String token = resolveToken(request);

        // 토큰이 유효한 경우 인증 정보 설정
        if (StringUtils.hasText(token) && jwtTokenProvider.validateAccessToken(token)) {
            String memberId = jwtTokenProvider.getMemberId(token);
            
            // 인증 정보 생성 및 SecurityContext에 저장
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    memberId, null, AuthorityUtils.NO_AUTHORITIES);
            
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        // 다음 필터로 요청 전달
        filterChain.doFilter(request, response);
    }

    // Authorization 헤더에서 JWT 토큰 추출
    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(TOKEN_PREFIX)) {
            return bearerToken.substring(TOKEN_PREFIX.length());
        }
        return null;
    }
}
