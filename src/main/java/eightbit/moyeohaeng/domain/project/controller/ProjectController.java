package eightbit.moyeohaeng.domain.project.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import eightbit.moyeohaeng.domain.auth.UserRole;
import eightbit.moyeohaeng.domain.auth.common.annotation.CurrentUserRole;
import eightbit.moyeohaeng.domain.auth.common.annotation.RequiredAccessRole;
import eightbit.moyeohaeng.domain.member.dto.response.MemberInfoResponse;
import eightbit.moyeohaeng.domain.project.common.success.ProjectSuccessCode;
import eightbit.moyeohaeng.domain.project.controller.swagger.ProjectApi;
import eightbit.moyeohaeng.domain.project.dto.ProjectDto;
import eightbit.moyeohaeng.domain.project.dto.condition.ProjectSearchCondition;
import eightbit.moyeohaeng.domain.project.dto.request.ProjectCreateRequest;
import eightbit.moyeohaeng.domain.project.dto.request.ProjectSortType;
import eightbit.moyeohaeng.domain.project.dto.request.ProjectUpdateRequest;
import eightbit.moyeohaeng.domain.project.dto.response.ProjectCreateResponse;
import eightbit.moyeohaeng.domain.project.dto.response.ProjectGetResponse;
import eightbit.moyeohaeng.domain.project.dto.response.ProjectSearchResponse;
import eightbit.moyeohaeng.domain.project.dto.response.ProjectUpdateResponse;
import eightbit.moyeohaeng.domain.project.service.ProjectService;
import eightbit.moyeohaeng.global.dto.UserInfo;
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
	@RequiredAccessRole(UserRole.MEMBER)
	public SuccessResponse<ProjectCreateResponse> create(
		@Valid @RequestBody ProjectCreateRequest request,
		@AuthenticationPrincipal CustomUserDetails currentUser) {
		ProjectDto createdProject = projectService.create(request, currentUser);
		return SuccessResponse.of(CommonSuccessCode.CREATE_SUCCESS, ProjectCreateResponse.from(createdProject));
	}

	@Override
	@PutMapping("/{projectId}")
	@RequiredAccessRole(UserRole.MEMBER)
	public SuccessResponse<ProjectUpdateResponse> update(@PathVariable Long projectId,
		@Valid @RequestBody ProjectUpdateRequest request,
		@AuthenticationPrincipal CustomUserDetails currentUser) {
		ProjectDto updatedProject = projectService.update(projectId, request, currentUser);
		return SuccessResponse.of(CommonSuccessCode.UPDATE_SUCCESS, ProjectUpdateResponse.from(updatedProject));
	}

	@Override
	@GetMapping
	// @RequiredAccessRole(UserRole.MEMBER) // TODO team또는 projectId가 필요함
	public SuccessResponse<ProjectSearchResponse> searchMyProjects(
		@AuthenticationPrincipal CustomUserDetails currentUser,
		@RequestParam(required = false) Long teamId,
		@RequestParam(required = false, defaultValue = "MODIFIED_AT_DESC") ProjectSortType sortType
	) {
		ProjectSearchCondition condition = ProjectSearchCondition.builder()
			.teamId(teamId)
			.sortType(sortType)
			.build();

		List<ProjectDto> projects = projectService.searchMyProjects(currentUser, condition);
		return SuccessResponse.of(CommonSuccessCode.SELECT_SUCCESS, ProjectSearchResponse.from(projects));
	}

	@Override
	@GetMapping("/{projectId}")
	public SuccessResponse<ProjectGetResponse> getById(
		@PathVariable Long projectId,
		@AuthenticationPrincipal CustomUserDetails currentUser
	) {
		ProjectDto projectDto = currentUser != null
			? projectService.getById(projectId, currentUser)
			: projectService.findById(projectId);

		return SuccessResponse.of(CommonSuccessCode.SELECT_SUCCESS, ProjectGetResponse.from(projectDto));
	}

	/**
	 * 프로젝트 연결 및 SSE 구독
	 */
	@Override
	@GetMapping(value = "/{projectId}/connect", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	@RequiredAccessRole(UserRole.VIEWER)
	public SseEmitter connectProject(
		@PathVariable Long projectId,
		@RequestHeader(value = "Last-Event-ID", required = false) String lastEventId,
		@AuthenticationPrincipal CustomUserDetails currentUser,
		@CurrentUserRole UserRole userRole
	) {
		UserInfo userInfo = currentUser.getUserInfo().withRole(userRole);
		return projectService.connect(projectId, lastEventId, userInfo);
	}

	@Override
	@GetMapping("/{projectId}/members")
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
	@RequiredAccessRole(UserRole.OWNER)
	public SuccessResponse<Void> delete(@PathVariable Long projectId,
		@AuthenticationPrincipal CustomUserDetails currentUser) {
		projectService.delete(projectId, currentUser);
		return SuccessResponse.of(CommonSuccessCode.DELETE_SUCCESS, null);
	}
}
