package eightbit.moyeohaeng.domain.selection.controller.swagger;

import eightbit.moyeohaeng.domain.selection.dto.request.PlaceBlockToGroupsRequest;
import eightbit.moyeohaeng.domain.selection.dto.request.PlaceGroupCreateRequest;
import eightbit.moyeohaeng.domain.selection.dto.response.PlaceGroupBlockResponse;
import eightbit.moyeohaeng.domain.selection.dto.response.PlaceGroupResponse;
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

@Tag(name = "장소 그룹 API", description = "장소 그룹 CRUD 작업을 처리하는 API")
public interface PlaceGroupApi {

	@Operation(summary = "장소 그룹 생성", description = "새로운 장소 그룹을 생성합니다.")
	@ApiResponses({
		@ApiResponse(responseCode = "201", description = "장소 그룹 생성 성공"),
		@ApiResponse(responseCode = "404", description = "장소 블록을 찾을 수 없음",
			content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
	})
	SuccessResponse<PlaceGroupResponse> create(
		@Parameter(description = "프로젝트 ID", required = true)
		Long projectId,
		PlaceGroupCreateRequest request,
		CustomUserDetails currentUser
	);

	@Operation(summary = "장소 그룹에 블록 추가/삭제", description = "장소 블록을 여러 장소 그룹에 추가/삭제합니다.")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "장소 그룹에 추가/삭제 성공"),
		@ApiResponse(responseCode = "404", description = "장소 블록 또는 장소 그룹을 찾을 수 없음",
			content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
	})
	SuccessResponse<PlaceGroupBlockResponse> updatePlaceBlockToGroups(
		@Parameter(description = "프로젝트 ID", required = true)
		Long projectId,
		Long placeBlockId,
		PlaceBlockToGroupsRequest request,
		CustomUserDetails currentUser
	);
}
