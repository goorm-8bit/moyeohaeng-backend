package eightbit.moyeohaeng.global.security;

import java.io.IOException;

import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import eightbit.moyeohaeng.domain.project.service.ProjectService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * 대상 프로젝트가 외부 공유를 허용하는 경우, "/sub/**" 경로의 비로그인 접근에 대해
 * 게스트 사용자 Principal을 생성하여 SecurityContext에 설정
 *  비로그인 사용자 요청에서도 {@code @AuthenticationPrincipal CustomUserDetails}
 *  사용 가능
 */
public class ShareGuestAuthenticationFilter extends OncePerRequestFilter {

	private final ProjectService projectService;

	public ShareGuestAuthenticationFilter(ProjectService projectService) {
		this.projectService = projectService;
	}

	@Override
	protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
		@NonNull FilterChain filterChain)
		throws ServletException, IOException {
		// 이미 인증된 사용자가 있는 경우 추가 처리를 하지 않음
		if (SecurityContextHolder.getContext().getAuthentication() != null) {
			filterChain.doFilter(request, response);
			return;
		}

		// 경로에서 projectId 해석 후, 공유 허용 시 게스트 인증 시도
		String path = request.getServletPath();
		Long projectId = resolveProjectId(path);
		if (projectId != null) {
			allowGuestAccess(projectId);
		}

		filterChain.doFilter(request, response);
	}

	@Override
	protected boolean shouldNotFilter(@NonNull HttpServletRequest request) throws ServletException {
		// "/sub/**" 에서만 동작하도록 함
		return !isSubPath(request.getServletPath());
	}

	private Long resolveProjectId(String path) {
		if (!StringUtils.hasText(path))
			return null;
		// 예상 경로 패턴:
		// /sub/v1/projects/{id}
		// /sub/v1/projects/{id}/...
		String[] segments = path.split("/");
		for (int i = 0; i < segments.length - 1; i++) {
			if ("projects".equals(segments[i])) {
				String cand = segments[i + 1];
				try {
					return Long.parseLong(cand);
				} catch (NumberFormatException ignored) {
				}
			}
		}
		return null;
	}

	private boolean isSubPath(String path) {
		return path != null && path.startsWith("/sub/");
	}

	private void allowGuestAccess(Long projectId) {
		try {
			// 해당 프로젝트가 공유 허용 상태인지 검사
			projectService.ensureShareAllowed(projectId);

			// 비로그인 접근을 나타내는 최소한의 게스트 Principal 생성
			CustomUserDetails guest = CustomUserDetails.guestOf("viewer", "anonymous");
			UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
				guest, null, guest.getAuthorities());
			SecurityContextHolder.getContext().setAuthentication(authentication);
		} catch (RuntimeException ignored) {
			//TODO 공유 허용이 아니거나 처리 중 오류 발생 시 로직 추가 
		}
	}
}
