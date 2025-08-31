package eightbit.moyeohaeng.domain.project.controller.swagger;

import org.springframework.http.ResponseEntity;

import eightbit.moyeohaeng.domain.project.dto.response.PlaceBlockLikeSummaryResponse;
import eightbit.moyeohaeng.global.exception.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "PlaceBlockLike", description = "장소 블록 좋아요 API - on/off 토글")
public interface PlaceBlockLikeApi {

	@Operation(summary = "장소 블록 좋아요 on/off",
		description = """
			특정 장소 블록에 대한 좋아요 상태를 토글합니다.
			- 이미 존재하는 like 튜플이 있으면 **새로 생성하지 않고** soft-delete 복구( deleted_at → null ) 또는 삭제로 전환합니다.
			- 응답은 총 좋아요 수 및 현재 사용자의 liked 여부를 포함합니다.
			"""
	)
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "토글 성공",
			content = @Content(schema = @Schema(implementation = PlaceBlockLikeSummaryResponse.class))),
		@ApiResponse(responseCode = "400", description = "잘못된 요청 데이터",
			content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
		@ApiResponse(responseCode = "403", description = "권한 없음",
			content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
		@ApiResponse(responseCode = "404", description = "프로젝트/장소블록 없음",
			content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
	})
	ResponseEntity<PlaceBlockLikeSummaryResponse> toggle(
		@Parameter(description = "프로젝트 ID", required = true, example = "1")
		Long projectId,
		@Parameter(description = "장소블록 ID", required = true, example = "10")
		Long placeBlockId
	);
}
