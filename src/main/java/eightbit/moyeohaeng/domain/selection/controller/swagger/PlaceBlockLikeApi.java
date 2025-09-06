package eightbit.moyeohaeng.domain.selection.controller.swagger;

import org.springframework.http.ResponseEntity;

import eightbit.moyeohaeng.domain.selection.dto.response.PlaceBlockLikeSummaryResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "장소 블록 좋아요 API", description = "좋아요 on/off")
public interface PlaceBlockLikeApi {

	@Operation(summary = "장소 블록 좋아요 토글", description = "해당 장소 블록에 좋아요를 누르거나 취소합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "좋아요 토글 성공"),
		@ApiResponse(responseCode = "404", description = "회원 또는 장소 블록을 찾을 수 없음")
	})
	ResponseEntity<PlaceBlockLikeSummaryResponse> toggleLike(
		@Parameter(description = "프로젝트 ID", required = true, example = "1") Long projectId,
		@Parameter(description = "장소 블록 ID", required = true, example = "10") Long placeBlockId,
		@Parameter(description = "회원 ID", required = true, example = "5") Long memberId
	);
}
