package eightbit.moyeohaeng.domain.project.controller;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import eightbit.moyeohaeng.domain.auth.UserRole;
import eightbit.moyeohaeng.domain.auth.annotation.RequiredAccessRole;
import eightbit.moyeohaeng.domain.member.dto.response.MemberInfoResponse;
import eightbit.moyeohaeng.domain.project.common.success.ProjectSuccessCode;
import eightbit.moyeohaeng.domain.project.controller.swagger.ProjectApi;
import eightbit.moyeohaeng.domain.project.dto.ProjectDto;
import eightbit.moyeohaeng.domain.project.dto.request.ProjectCreateRequest;
import eightbit.moyeohaeng.domain.project.dto.request.ProjectUpdateRequest;
import eightbit.moyeohaeng.domain.project.service.ProjectService;
import eightbit.moyeohaeng.global.security.CustomUserDetails;
import eightbit.moyeohaeng.global.success.CommonSuccessCode;
import eightbit.moyeohaeng.global.success.SuccessResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping({"/v1/projects", "/sub/v1/projects"})
public class ProjectController implements ProjectApi {

	private final ProjectService projectService;

	@Override
	@PostMapping
	@PreAuthorize("isAuthenticated()")
	@RequiredAccessRole(UserRole.MEMBER)
	public SuccessResponse<ProjectDto> create(@Valid @RequestBody ProjectCreateRequest request,
		@AuthenticationPrincipal CustomUserDetails currentUser) {
		ProjectDto response = projectService.create(request, currentUser);
		return SuccessResponse.of(ProjectSuccessCode.CREATE_PROJECT, response);
	}

	@Override
	@PutMapping("/{projectId}")
	@PreAuthorize("isAuthenticated()")
	@RequiredAccessRole(UserRole.MEMBER)
	public SuccessResponse<ProjectDto> update(
		@PathVariable Long projectId,
		@Valid @RequestBody ProjectUpdateRequest request,
		@AuthenticationPrincipal CustomUserDetails currentUser) {
		ProjectDto updatedProject = projectService.update(projectId, request, currentUser);
		return SuccessResponse.of(CommonSuccessCode.UPDATE_SUCCESS, updatedProject);
	}

	@Override
	@GetMapping
	@PreAuthorize("isAuthenticated()")
	public SuccessResponse<List<ProjectDto>> get(
		@AuthenticationPrincipal CustomUserDetails currentUser) {
		List<ProjectDto> projects = projectService.findWithMe(currentUser);
		return SuccessResponse.of(CommonSuccessCode.SELECT_SUCCESS, projects);
	}

	@Override
	@GetMapping("/{projectId}")
	public SuccessResponse<ProjectDto> getById(@PathVariable Long projectId,
		@AuthenticationPrincipal CustomUserDetails currentUser) {
		if (currentUser == null) {
			projectService.ensureShareAllowed(projectId);
		}
		ProjectDto project = projectService.findById(projectId);
		return SuccessResponse.of(CommonSuccessCode.SELECT_SUCCESS, project);
	}

	/**
	 * 프로젝트 연결 및 SSE 구독
	 */
	@Override
	@GetMapping(value = "/{projectId}/connect", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	public SseEmitter connectProject(@PathVariable Long projectId,
		@AuthenticationPrincipal CustomUserDetails currentUser) {

		if (currentUser == null) {
			projectService.ensureShareAllowed(projectId);
		}

		// TODO: emitter registry에 등록하고 권한 검증(프로젝트 접근 가능 여부) 수행
		SseEmitter emitter = new SseEmitter(Duration.ofMinutes(30).toMillis());
		emitter.onTimeout(emitter::complete);
		emitter.onCompletion(() -> {
			// TODO: registry에서 제거
		});
		return emitter;
	}

	@Override
	@GetMapping("/{projectId}/members")
	@org.springframework.security.access.prepost.PreAuthorize("isAuthenticated()")
	@RequiredAccessRole(UserRole.MEMBER)
	public SuccessResponse<List<MemberInfoResponse>> getConnectedMember(
		@PathVariable Long projectId,
		@AuthenticationPrincipal CustomUserDetails currentUser
	) {

		List<MemberInfoResponse> responses = new ArrayList<>();
		return SuccessResponse.of(ProjectSuccessCode.CONNECTING_MEMBERS, responses);
	}

	@Override
	@DeleteMapping("/{projectId}")
	@org.springframework.security.access.prepost.PreAuthorize("isAuthenticated()")
	@RequiredAccessRole(UserRole.OWNER)
	public SuccessResponse<Void> delete(@PathVariable Long projectId,
		@AuthenticationPrincipal CustomUserDetails currentUser) {
		projectService.delete(projectId, currentUser);
		return SuccessResponse.of(CommonSuccessCode.DELETE_SUCCESS, null);
	}
}
