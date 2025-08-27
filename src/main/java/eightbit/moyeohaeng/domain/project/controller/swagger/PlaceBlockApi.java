package eightbit.moyeohaeng.domain.project.controller.swagger;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import eightbit.moyeohaeng.domain.project.dto.request.PlaceBlockCreateRequest;
import eightbit.moyeohaeng.domain.project.dto.request.PlaceBlockUpdateRequest;
import eightbit.moyeohaeng.domain.project.dto.response.PlaceBlockResponse;
import eightbit.moyeohaeng.global.dto.PageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

/**
 * 장소블록 CRUD API 명세.
 * 권한: 생성(OWNER), 조회(모든 역할), 수정·삭제(OWNER/EDITOR).
 * 개수 제한: 프로젝트당 최대 100개.
 */
@Tag(name = "PlaceBlock")
@RequestMapping("/v1/projects/{projectId}/place-blocks")
public interface PlaceBlockApi {

	@Operation(summary = "장소블록 생성 (좌표 포함)")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "201", description = "장소블록 생성 성공"),
		@ApiResponse(responseCode = "400", description = "잘못된 요청 데이터"),
		@ApiResponse(responseCode = "403", description = "권한 없음"),
		@ApiResponse(responseCode = "409", description = "프로젝트당 최대 개수(100개) 초과")
	})
	ResponseEntity<PlaceBlockResponse> create(
		@PathVariable Long projectId,
		@Valid @RequestBody PlaceBlockCreateRequest request
	);

	@Operation(summary = "장소블록 단건 조회")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "장소블록 조회 성공"),
		@ApiResponse(responseCode = "404", description = "장소블록을 찾을 수 없음")
	})
	ResponseEntity<PlaceBlockResponse> get(
		@PathVariable Long projectId,
		@PathVariable Long placeBlockId
	);

	@Operation(summary = "장소블록 목록 조회(페이지)")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "장소블록 목록 조회 성공")
	})
	ResponseEntity<PageResponse<PlaceBlockResponse>> list(
		@PathVariable Long projectId,
		@org.springdoc.core.annotations.ParameterObject Pageable pageable
	);

	@Operation(summary = "장소블록 수정")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "장소블록 수정 성공"),
		@ApiResponse(responseCode = "400", description = "잘못된 요청 데이터"),
		@ApiResponse(responseCode = "403", description = "권한 없음"),
		@ApiResponse(responseCode = "404", description = "장소블록을 찾을 수 없음")
	})
	ResponseEntity<PlaceBlockResponse> update(
		@PathVariable Long projectId,
		@PathVariable Long placeBlockId,
		@Valid @RequestBody PlaceBlockUpdateRequest request
	);

	@Operation(summary = "장소블록 삭제")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "204", description = "장소블록 삭제 성공"),
		@ApiResponse(responseCode = "403", description = "권한 없음"),
		@ApiResponse(responseCode = "404", description = "장소블록을 찾을 수 없음")
	})
	ResponseEntity<Void> delete(
		@PathVariable Long projectId,
		@PathVariable Long placeBlockId
	);
}
