package eightbit.moyeohaeng.global.security;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import eightbit.moyeohaeng.domain.member.entity.member.Member;
import eightbit.moyeohaeng.domain.member.service.MemberService;
import eightbit.moyeohaeng.global.domain.auth.JwtTokenProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

/**
 * JWT 토큰의 유효성을 검증하고 인증 정보를 설정하는 필터
 */
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private final JwtTokenProvider jwtTokenProvider;
	private final MemberService memberService;
	private static final String AUTHORIZATION_HEADER = "Authorization";
	private static final String TOKEN_PREFIX = "Bearer ";

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
		throws ServletException, IOException {

		// 요청 헤더에서 JWT 토큰 추출
		String token = resolveToken(request);

		// 토큰이 유효한 경우 인증 정보 설정
		if (StringUtils.hasText(token) && jwtTokenProvider.validateAccessToken(token)) {
			String memberIdStr = jwtTokenProvider.getMemberId(token);
			Long memberId = null;
			try {
				memberId = Long.valueOf(memberIdStr);
			} catch (NumberFormatException ignored) {
			}

			if (memberId != null) {
				try {
					// TODO 프로젝트 외부 공유 허용시에 프로젝트의 quest 생성
					// 만약에 quest 생성이 안 될경우 동작하는 코드
					Member member = memberService.findById(memberId);
					CustomUserDetails user = CustomUserDetails.from(member);

					// 인증 정보 생성 및 SecurityContext에 저장
					UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
						user, null, user.getAuthorities());

					SecurityContextHolder.getContext().setAuthentication(authentication);
				} catch (RuntimeException exception) {
					//TODO 사용자 미존재,삭제 등: 인증 미설정 상태로 계속 진행
				}
			}
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
