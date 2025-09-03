package eightbit.moyeohaeng.domain.selection.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import eightbit.moyeohaeng.domain.selection.controller.swagger.PlaceBlockApi;
import eightbit.moyeohaeng.domain.selection.dto.request.PlaceBlockCreateRequest;
import eightbit.moyeohaeng.domain.selection.dto.request.PlaceBlockUpdateRequest;
import eightbit.moyeohaeng.domain.selection.dto.response.PlaceBlockResponse;
import eightbit.moyeohaeng.domain.selection.service.PlaceBlockService;
import eightbit.moyeohaeng.global.dto.PageResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * PlaceBlock REST 컨트롤러.
 * 인터페이스(PlaceBlockApi) 구현 + 보안 연동 전 임시 사용자 컨텍스트 제공.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/projects/{projectId}/place-blocks")
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
	@PostMapping
	public ResponseEntity<PlaceBlockResponse> create(@PathVariable("projectId") Long projectId,
		@Valid @RequestBody PlaceBlockCreateRequest request) {
		var res = service.create(projectId, currentUserId(), currentUserRole(projectId), request);
		var location = UriComponentsBuilder.fromPath("/v1/projects/{projectId}/place-blocks/{id}")
			.buildAndExpand(projectId, res.id())
			.toUri();
		return ResponseEntity.created(location).body(res);
	}

	@Override
	@GetMapping("/{placeBlockId}")
	public ResponseEntity<PlaceBlockResponse> get(@PathVariable("projectId") Long projectId,
		@PathVariable("placeBlockId") Long placeBlockId) {
		return ResponseEntity.ok(service.get(projectId, placeBlockId));
	}

	@Override
	@GetMapping
	public ResponseEntity<PageResponse<PlaceBlockResponse>> list(@PathVariable("projectId") Long projectId,
		Pageable pageable) {
		return ResponseEntity.ok(service.getPages(projectId, pageable));
	}

	@Override
	@PatchMapping("/{placeBlockId}")
	public ResponseEntity<PlaceBlockResponse> update(@PathVariable("projectId") Long projectId,
		@PathVariable("placeBlockId") Long placeBlockId,
		@Valid @RequestBody PlaceBlockUpdateRequest request) {
		var res = service.update(projectId, placeBlockId, currentUserId(), currentUserRole(projectId), request);
		return ResponseEntity.ok(res);
	}

	@Override
	@DeleteMapping("/{placeBlockId}")
	public ResponseEntity<Void> delete(@PathVariable("projectId") Long projectId,
		@PathVariable("placeBlockId") Long placeBlockId) {
		service.delete(projectId, placeBlockId, currentUserId(), currentUserRole(projectId));
		return ResponseEntity.noContent().build();
	}
}
