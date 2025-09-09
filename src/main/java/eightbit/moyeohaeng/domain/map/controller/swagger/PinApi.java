package eightbit.moyeohaeng.domain.map.controller.swagger;

import java.util.List;

import eightbit.moyeohaeng.domain.map.dto.request.PinCreateRequest;
import eightbit.moyeohaeng.domain.map.dto.response.PinResponse;
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

@Tag(name = "핀 API", description = "핀 CRUD 작업을 처리하는 API")
public interface PinApi {

	@Operation(summary = "핀 생성", description = "프로젝트에 핀을 생성합니다.")
	@ApiResponses({
		@ApiResponse(responseCode = "201", description = "핀 생성 성공",
			content = @Content(schema = @Schema(implementation = SuccessResponse.class))),
		@ApiResponse(responseCode = "404", description = "프로젝트 없음",
			content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
		@ApiResponse(responseCode = "409", description = "중복된 핀",
			content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
	})
	SuccessResponse<PinResponse> create(
		@Parameter(description = "프로젝트 ID", required = true)
		Long projectId,
		@Parameter(description = "생성 요청 바디", required = true)
		PinCreateRequest request,
		CustomUserDetails currentUser
	);

	@Operation(summary = "핀 조회", description = "프로젝트의 모든 핀을 조회합니다.")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "핀 목록 조회 성공",
			content = @Content(schema = @Schema(implementation = SuccessResponse.class))),
		@ApiResponse(responseCode = "404", description = "프로젝트 없음",
			content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
	})
	SuccessResponse<List<PinResponse>> getPins(
		@Parameter(description = "프로젝트 ID", required = true)
		Long projectId
	);

	@Operation(summary = "핀 제거", description = "특정 핀을 제거합니다. (소프트 삭제)")
	@ApiResponses({
		@ApiResponse(responseCode = "204", description = "제거 성공"),
		@ApiResponse(responseCode = "404", description = "프로젝트/핀 없음",
			content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
	})
	SuccessResponse<Void> delete(
		@Parameter(description = "프로젝트 ID", required = true)
		Long projectId,
		@Parameter(description = "핀 ID", required = true)
		Long pinId
	);
}
