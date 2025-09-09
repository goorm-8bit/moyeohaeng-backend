package eightbit.moyeohaeng.domain.selection.controller.swagger;

import java.util.List;

import eightbit.moyeohaeng.domain.selection.dto.request.PlaceBlockCreateRequest;
import eightbit.moyeohaeng.domain.selection.dto.request.PlaceBlockUpdateMemoRequest;
import eightbit.moyeohaeng.domain.selection.dto.response.PlaceBlockCreateResponse;
import eightbit.moyeohaeng.domain.selection.dto.response.PlaceBlockSearchResponse;
import eightbit.moyeohaeng.domain.selection.dto.response.PlaceBlockUpdateMemoResponse;
import eightbit.moyeohaeng.global.exception.ErrorResponse;
import eightbit.moyeohaeng.global.security.CustomUserDetails;
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

	@Operation(summary = "메모 수정", description = "장소 블록에 메모를 작성합니다.")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "메모 수정 성공"),
		@ApiResponse(responseCode = "404", description = "장소 블록을 찾을 수 없음",
			content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
	})
	SuccessResponse<PlaceBlockUpdateMemoResponse> updateMemo(
		@Parameter(description = "프로젝트 ID", required = true)
		Long projectId,
		@Parameter(description = "장소 블록 ID", required = true)
		Long placeBlockId,
		PlaceBlockUpdateMemoRequest request
	);

	@Operation(summary = "장소 블록 목록 조회", description = "장소 블록 목록을 조회합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "장소 블록 목록 조회 성공")
	})
	SuccessResponse<List<PlaceBlockSearchResponse>> searchPlaceBlocks(
		@Parameter(description = "프로젝트 ID", required = true)
		Long projectId,
		CustomUserDetails currentUser
	);

	@Operation(summary = "장소 블록 삭제")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "204", description = "장소 블록 삭제 성공"),
		@ApiResponse(responseCode = "404", description = "장소 블록을 찾을 수 없음",
			content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
	})
	SuccessResponse<Void> delete(
		@Parameter(description = "프로젝트 ID", required = true)
		Long projectId,
		@Parameter(description = "장소 블록 ID", required = true)
		Long placeBlockId
	);
}
