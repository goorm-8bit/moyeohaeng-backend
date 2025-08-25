package eightbit.moyeohaeng.domain.project.plan.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import eightbit.moyeohaeng.domain.project.plan.controller.swagger.PlaceBlockApi;
import eightbit.moyeohaeng.domain.project.plan.dto.request.PlaceBlockCreateRequest;
import eightbit.moyeohaeng.domain.project.plan.dto.request.PlaceBlockUpdateRequest;
import eightbit.moyeohaeng.domain.project.plan.dto.response.PlaceBlockPageResponse;
import eightbit.moyeohaeng.domain.project.plan.dto.response.PlaceBlockResponse;
import eightbit.moyeohaeng.domain.project.plan.service.PlaceBlockService;
import lombok.RequiredArgsConstructor;

/**
 * PlaceBlock REST 컨트롤러.
 * 인터페이스(PlaceBlockApi) 구현 + 보안 연동 전 임시 사용자 컨텍스트 제공.
 */
@RestController
@RequiredArgsConstructor
public class PlaceBlockController implements PlaceBlockApi {

	private final PlaceBlockService service;

	// TODO: Security 연동 시 교체
	private Long currentUserId() {
		return 1L;
	}

	private String currentUserRole(Long projectId) {
		return "OWNER";
	}

	@Override
	public ResponseEntity<PlaceBlockResponse> create(Long projectId, PlaceBlockCreateRequest request) {
		var res = service.create(projectId, currentUserId(), currentUserRole(projectId), request);
		return ResponseEntity
			.created(java.net.URI.create(String.format("/v1/projects/%d/place-blocks/%d", projectId, res.id())))
			.body(res);
	}

	@Override
	public ResponseEntity<PlaceBlockResponse> get(Long projectId, Long placeBlockId) {
		return ResponseEntity.ok(service.get(projectId, placeBlockId));
	}

	@Override
	public ResponseEntity<PlaceBlockPageResponse> list(Long projectId, Pageable pageable) {
		return ResponseEntity.ok(service.list(projectId, pageable));
	}

	@Override
	public ResponseEntity<PlaceBlockResponse> update(Long projectId, Long placeBlockId,
		PlaceBlockUpdateRequest request) {
		var res = service.update(projectId, placeBlockId, currentUserId(), currentUserRole(projectId), request);
		return ResponseEntity.ok(res);
	}

	@Override
	public ResponseEntity<Void> delete(Long projectId, Long placeBlockId) {
		service.delete(projectId, placeBlockId, currentUserId(), currentUserRole(projectId));
		return ResponseEntity.noContent().build();
	}
}