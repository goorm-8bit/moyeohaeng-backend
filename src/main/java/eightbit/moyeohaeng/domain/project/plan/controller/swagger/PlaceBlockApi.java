package eightbit.moyeohaeng.domain.project.plan.controller.swagger;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import eightbit.moyeohaeng.domain.project.plan.dto.request.PlaceBlockCreateRequest;
import eightbit.moyeohaeng.domain.project.plan.dto.request.PlaceBlockUpdateRequest;
import eightbit.moyeohaeng.domain.project.plan.dto.response.PlaceBlockPageResponse;
import eightbit.moyeohaeng.domain.project.plan.dto.response.PlaceBlockResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "PlaceBlock")
@RequestMapping("/v1/projects/{projectId}/place-blocks")
public interface PlaceBlockApi {

	@Operation(summary = "장소블록 생성 (좌표 포함)")
	@PostMapping
	ResponseEntity<PlaceBlockResponse> create(
		@PathVariable Long projectId,
		@Valid @RequestBody PlaceBlockCreateRequest request
	);

	@Operation(summary = "장소블록 단건 조회")
	@GetMapping("/{placeBlockId}")
	ResponseEntity<PlaceBlockResponse> get(
		@PathVariable Long projectId,
		@PathVariable Long placeBlockId
	);

	@Operation(summary = "장소블록 목록 조회(페이지)")
	@GetMapping
	ResponseEntity<PlaceBlockPageResponse> list(
		@PathVariable Long projectId,
		@org.springdoc.core.annotations.ParameterObject Pageable pageable
	);

	@Operation(summary = "장소블록 수정")
	@PatchMapping("/{placeBlockId}")
	ResponseEntity<PlaceBlockResponse> update(
		@PathVariable Long projectId,
		@PathVariable Long placeBlockId,
		@Valid @RequestBody PlaceBlockUpdateRequest request
	);

	@Operation(summary = "장소블록 삭제")
	@DeleteMapping("/{placeBlockId}")
	ResponseEntity<Void> delete(
		@PathVariable Long projectId,
		@PathVariable Long placeBlockId
	);
}

