package eightbit.moyeohaeng.domain.selection.controller.swagger;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

import eightbit.moyeohaeng.domain.selection.dto.request.PlaceBlockCreateRequest;
import eightbit.moyeohaeng.domain.selection.dto.response.PlaceBlockCreateResponse;
import eightbit.moyeohaeng.domain.selection.dto.response.PlaceBlockResponse;
import eightbit.moyeohaeng.global.dto.PageResponse;
import eightbit.moyeohaeng.global.exception.ErrorResponse;
import eightbit.moyeohaeng.global.success.SuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * 장소 블록 CRUD API 명세.
 * 개수 제한: 프로젝트당 최대 100개.
 */
@Tag(name = "장소 블록 API", description = "장소 블록 CRUD 작업을 처리하는 API")
public interface PlaceBlockApi {

	@Operation(summary = "장소 블록 생성", description = "장소 블록을 생성합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "201", description = "장소 블록 생성 성공"),
		@ApiResponse(responseCode = "404", description = "장소 또는 프로젝트를 찾을 수 없음"),
		@ApiResponse(responseCode = "409", description = "프로젝트당 최대 개수(100개) 초과")
	})
	SuccessResponse<PlaceBlockCreateResponse> create(
		@Parameter(description = "프로젝트 ID", required = true)
		Long projectId,
		PlaceBlockCreateRequest request
	);

	@Operation(summary = "장소블록 목록 조회(페이지)")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "장소블록 목록 조회 성공")
	})
	ResponseEntity<PageResponse<PlaceBlockResponse>> list(
		@PathVariable Long projectId,
		@org.springdoc.core.annotations.ParameterObject Pageable pageable
	);

	@Operation(summary = "장소 블록 삭제")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "204", description = "장소 블록 삭제 성공"),
		@ApiResponse(responseCode = "404", description = "장소 블록을 찾을 수 없음",
			content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
	})
	SuccessResponse<Void> delete(
		@Parameter(description = "프로젝트 ID", required = true)
		@PathVariable Long projectId,
		@Parameter(description = "장소 블록 ID", required = true)
		@PathVariable Long placeBlockId
	);
}
