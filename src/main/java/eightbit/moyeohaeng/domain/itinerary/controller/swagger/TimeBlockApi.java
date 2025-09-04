package eightbit.moyeohaeng.domain.itinerary.controller.swagger;

import eightbit.moyeohaeng.domain.itinerary.dto.request.TimeBlockCreateRequest;
import eightbit.moyeohaeng.domain.itinerary.dto.request.TimeBlockUpdateRequest;
import eightbit.moyeohaeng.domain.itinerary.dto.response.TimeBlockResponse;
import eightbit.moyeohaeng.global.exception.ErrorResponse;
import eightbit.moyeohaeng.global.success.SuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "시간 블록 API", description = "시간 블록 CRUD 작업을 처리하는 API")
public interface TimeBlockApi {

	@Operation(summary = "시간 블록 생성", description = "여행 일정에 새로운 블록을 생성합니다.")
	@ApiResponses({
		@ApiResponse(responseCode = "201", description = "시간 블록 생성 성공"),
		@ApiResponse(responseCode = "404", description = "장소 또는 프로젝트를 찾을 수 없음",
			content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
		@ApiResponse(responseCode = "409", description = "다른 일정과 시간이 겹침",
			content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
	})
	SuccessResponse<TimeBlockResponse> create(
		@Parameter(description = "프로젝트 ID", required = true)
		Long projectId,
		TimeBlockCreateRequest request
	);

	@Operation(summary = "시간 블록 수정", description = "시간 블록의 일차, 시작 시간, 종료 시간, 메모를 수정합니다.")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "시간 블록 수정 성공"),
		@ApiResponse(responseCode = "404", description = "시간 블록을 찾을 수 없음"),
		@ApiResponse(responseCode = "409", description = "다른 일정과 시간이 겹침",
			content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
	})
	SuccessResponse<TimeBlockResponse> update(
		@Parameter(description = "프로젝트 ID", required = true)
		Long projectId,
		@Parameter(description = "시간 블록 ID", required = true)
		Long timeBlockId,
		TimeBlockUpdateRequest request
	);
}
