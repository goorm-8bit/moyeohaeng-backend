package eightbit.moyeohaeng.domain.project.controller;

import java.time.Duration;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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

import eightbit.moyeohaeng.domain.project.controller.swagger.ProjectApi;
import eightbit.moyeohaeng.domain.project.dto.ProjectDto;
import eightbit.moyeohaeng.domain.project.dto.request.ProjectCreateRequest;
import eightbit.moyeohaeng.domain.project.dto.request.ProjectUpdateRequest;
import eightbit.moyeohaeng.domain.project.service.ProjectService;
import eightbit.moyeohaeng.global.security.CustomUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
@RequestMapping(value = "/{projectId}/connect", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
public class ProjectController implements ProjectApi {

	private final ProjectService projectService;

	@Override
	@PostMapping
	public ResponseEntity<ProjectDto> create(@Valid @RequestBody ProjectCreateRequest request,
		@AuthenticationPrincipal CustomUserDetails currentUser) {
		ProjectDto response = projectService.create(request, currentUser);
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}

	@Override
	@PutMapping("/{projectId}")
	public ResponseEntity<ProjectDto> update(
		@PathVariable Long projectId,
		@Valid @RequestBody ProjectUpdateRequest request,
		@AuthenticationPrincipal CustomUserDetails currentUser) {
		ProjectDto updatedProject = projectService.update(projectId, request, currentUser);
		return ResponseEntity.ok(updatedProject);
	}

	@Override
	@GetMapping
	public ResponseEntity<List<ProjectDto>> getProjects(
		@AuthenticationPrincipal CustomUserDetails currentUser) {
		List<ProjectDto> projects = projectService.findByMember(currentUser);
		return ResponseEntity.ok(projects);
	}

	@Override
	@GetMapping("/{projectId}")
	public ResponseEntity<ProjectDto> getById(@PathVariable Long projectId,
		@AuthenticationPrincipal CustomUserDetails currentUser) {
		ProjectDto project = projectService.findById(projectId);
		return ResponseEntity.ok(project);
	}

	/**
	 * 프로젝트 연결 및 SSE 구독
	 */
	@Override
	@GetMapping("/{projectId}/connect")
	public SseEmitter connectProject(@PathVariable Long projectId,
		@AuthenticationPrincipal CustomUserDetails currentUser) {

		// TODO: emitter registry에 등록하고 권한 검증(프로젝트 접근 가능 여부) 수행
		SseEmitter emitter = new SseEmitter(Duration.ofMinutes(30).toMillis());
		emitter.onTimeout(emitter::complete);
		emitter.onCompletion(() -> {
			// TODO: registry에서 제거
		});
		return emitter;
	}

	@Override
	@DeleteMapping("/{projectId}")
	public ResponseEntity<Void> delete(@PathVariable Long projectId,
		@AuthenticationPrincipal CustomUserDetails currentUser) {
		projectService.delete(projectId, currentUser);
		return ResponseEntity.noContent().build();
	}
}
