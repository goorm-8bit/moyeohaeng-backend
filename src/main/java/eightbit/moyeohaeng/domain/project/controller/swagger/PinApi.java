package eightbit.moyeohaeng.domain.project.controller.swagger;

import java.util.List;

import org.springframework.http.ResponseEntity;

import eightbit.moyeohaeng.domain.project.dto.request.PinCreateRequest;
import eightbit.moyeohaeng.domain.project.dto.response.PinResponse;
import eightbit.moyeohaeng.global.exception.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Pin", description = "핀 API - 생성, 조회, 제거")
public interface PinApi {

	@Operation(summary = "핀 생성", description = "프로젝트에 핀을 생성합니다.")
	@ApiResponses({
		@ApiResponse(responseCode = "201", description = "핀 생성 성공",
			content = @Content(schema = @Schema(implementation = PinResponse.class))),
		@ApiResponse(responseCode = "400", description = "잘못된 요청 데이터",
			content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
		@ApiResponse(responseCode = "404", description = "프로젝트 없음",
			content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
	})
	ResponseEntity<PinResponse> create(
		@Parameter(description = "프로젝트 ID", required = true, example = "1")
		Long projectId,
		PinCreateRequest request
	);

	@Operation(summary = "핀 조회", description = "프로젝트의 모든 핀을 조회합니다.")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "조회 성공",
			content = @Content(array = @ArraySchema(schema = @Schema(implementation = PinResponse.class)))),
		@ApiResponse(responseCode = "404", description = "프로젝트 없음",
			content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
	})
	ResponseEntity<List<PinResponse>> list(
		@Parameter(description = "프로젝트 ID", required = true, example = "1")
		Long projectId
	);

	@Operation(summary = "핀 제거", description = "특정 핀을 제거합니다. (소프트 삭제)")
	@ApiResponses({
		@ApiResponse(responseCode = "204", description = "제거 성공"),
		@ApiResponse(responseCode = "404", description = "프로젝트/핀 없음",
			content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
	})
	ResponseEntity<Void> delete(
		@Parameter(description = "프로젝트 ID", required = true, example = "1")
		Long projectId,
		@Parameter(description = "핀 ID", required = true, example = "10")
		Long pinId
	);
}
