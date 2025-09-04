package eightbit.moyeohaeng.domain.selection.controller.swagger;

import java.util.List;

import eightbit.moyeohaeng.domain.selection.dto.request.PlaceBlockToGroupsRequest;
import eightbit.moyeohaeng.domain.selection.dto.request.PlaceGroupRequest;
import eightbit.moyeohaeng.domain.selection.dto.request.PlaceGroupUpdateMemoRequest;
import eightbit.moyeohaeng.domain.selection.dto.response.PlaceGroupBlockResponse;
import eightbit.moyeohaeng.domain.selection.dto.response.PlaceGroupResponse;
import eightbit.moyeohaeng.domain.selection.dto.response.PlaceGroupUpdateMemoResponse;
import eightbit.moyeohaeng.global.exception.ErrorResponse;
import eightbit.moyeohaeng.global.success.SuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "장소 그룹 API", description = "장소 그룹 CRUD 작업을 처리하는 API")
public interface PlaceGroupApi {

	@Operation(summary = "장소 그룹 생성", description = "새로운 장소 그룹을 생성합니다.")
	@ApiResponses({
		@ApiResponse(responseCode = "201", description = "장소 그룹 생성 성공"),
		@ApiResponse(responseCode = "404", description = "프로젝트 또는 장소 블록을 찾을 수 없음",
			content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
	})
	SuccessResponse<PlaceGroupResponse> create(
		@Parameter(description = "프로젝트 ID", required = true)
		Long projectId,
		PlaceGroupRequest request
	);

	@Operation(summary = "장소 블록에 그룹 추가/삭제", description = "장소 블록을 여러 장소 그룹에 추가/삭제합니다.")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "그룹 추가/삭제 성공"),
		@ApiResponse(responseCode = "404", description = "장소 블록 또는 장소 그룹을 찾을 수 없음",
			content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
	})
	SuccessResponse<PlaceGroupBlockResponse> updatePlaceBlockToGroups(
		@Parameter(description = "프로젝트 ID", required = true)
		Long projectId,
		@Parameter(description = "장소 블록 ID", required = true)
		Long placeBlockId,
		PlaceBlockToGroupsRequest request
	);

	@Operation(summary = "장소 그룹 수정", description = "장소 그룹의 이름, 색상, 블록을 수정합니다.")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "장소 그룹 수정 성공"),
		@ApiResponse(responseCode = "404", description = "장소 그룹을 찾을 수 없음",
			content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
	})
	SuccessResponse<PlaceGroupResponse> update(
		@Parameter(description = "프로젝트 ID", required = true)
		Long projectId,
		@Parameter(description = "장소 그룹 ID", required = true)
		Long placeGroupId,
		PlaceGroupRequest request
	);

	@Operation(summary = "메모 수정", description = "장소 그룹에 메모를 작성합니다.")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "메모 수정 성공"),
		@ApiResponse(responseCode = "404", description = "장소 그룹을 찾을 수 없음",
			content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
	})
	SuccessResponse<PlaceGroupUpdateMemoResponse> updateMemo(
		@Parameter(description = "프로젝트 ID", required = true)
		Long projectId,
		@Parameter(description = "장소 그룹 ID", required = true)
		Long placeGroupId,
		PlaceGroupUpdateMemoRequest request
	);

	@Operation(summary = "장소 그룹 목록 조회", description = "장소 그룹 목록을 조회합니다.")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "장소 그룹 목록 조회 성공")
	})
	SuccessResponse<List<PlaceGroupResponse>> getPlaceGroups(
		@Parameter(description = "프로젝트 ID", required = true)
		Long projectId
	);

	@Operation(summary = "장소 그룹 삭제", description = "장소 그룹을 삭제합니다.")
	@ApiResponses({
		@ApiResponse(responseCode = "204", description = "장소 그룹 삭제 성공"),
		@ApiResponse(responseCode = "404", description = "장소 그룹을 찾을 수 없음",
			content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
	})
	SuccessResponse<Void> delete(
		@Parameter(description = "프로젝트 ID", required = true)
		Long projectId,
		@Parameter(description = "장소 그룹 ID", required = true)
		Long placeGroupId
	);
}
