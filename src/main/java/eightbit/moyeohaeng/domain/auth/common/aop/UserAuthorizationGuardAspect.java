package eightbit.moyeohaeng.domain.auth.common.aop;

import java.util.Map;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.HandlerMapping;

import eightbit.moyeohaeng.domain.auth.UserRole;
import eightbit.moyeohaeng.domain.auth.common.annotation.RequiredAccessRole;
import eightbit.moyeohaeng.domain.auth.service.UserAuthorizationService;
import eightbit.moyeohaeng.domain.project.dto.request.ProjectCreateRequest;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Aspect
@Component
@RequiredArgsConstructor
public class UserAuthorizationGuardAspect {

	private final UserAuthorizationService accessService;

	@Before("@annotation(required)")
	public void checkAccess(JoinPoint jp, RequiredAccessRole required) {
		HttpServletRequest request = currentRequest();
		if (request == null) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "요청 정보를 확인할 수 없습니다.");
		}
		Long projectId = extractProjectId(request, jp.getArgs());
		UserRole actual;
		if (projectId != null) {
			actual = accessService.resolveProjectRole(projectId, request);
		} else {
			Long teamId = extractTeamId(request, jp.getArgs());
			if (teamId == null) {
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "projectId 또는 teamId가 필요합니다.");
			}
			actual = accessService.resolveTeamRole(teamId, request);
		}
		if (!UserAuthorizationService.isAllowed(actual, required.value())) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "권한이 없습니다. 최소 요구 권한: " + required.value());
		}
	}

	private HttpServletRequest currentRequest() {
		RequestAttributes attrs = RequestContextHolder.getRequestAttributes();
		if (attrs instanceof ServletRequestAttributes sra) {
			return sra.getRequest();
		}
		return null;
	}

	private Long extractProjectId(HttpServletRequest request, Object[] args) {
		Object attr = request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
		if (attr instanceof Map<?, ?> map) {
			Object v = map.get("projectId");
			Long id = coerceLong(v);
			if (id != null)
				return id;
		}
		String q = request.getParameter("projectId");
		Long id = coerceLong(q);
		if (id != null)
			return id;
		for (Object a : args) {
			if (a instanceof Long l)
				return l;
			if (a instanceof String s) {
				Long c = coerceLong(s);
				if (c != null)
					return c;
			}
		}
		return null;
	}

	private Long extractTeamId(HttpServletRequest request, Object[] args) {
		Object attr = request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
		if (attr instanceof Map<?, ?> map) {
			Object v = map.get("teamId");
			Long id = coerceLong(v);
			if (id != null)
				return id;
		}
		String q = request.getParameter("teamId");
		Long id = coerceLong(q);
		if (id != null)
			return id;
		for (Object a : args) {
			if (a instanceof ProjectCreateRequest req) {
				return req.teamId();
			}
		}
		return null;
	}

	private Long coerceLong(Object v) {
		if (v == null)
			return null;
		if (v instanceof Number n)
			return n.longValue();
		if (v instanceof String s) {
			try {
				return Long.parseLong(s);
			} catch (NumberFormatException ignored) {
			}
		}
		return null;
	}
}
