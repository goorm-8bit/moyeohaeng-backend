package eightbit.moyeohaeng.domain.project.dto.response;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "장소 블록 좋아요 상태 응답")
public record PlaceBlockLikeSummaryResponse(
	@Schema(description = "총 좋아요 수")
	Integer totalCount,

	@Schema(description = "요청 사용자의 좋아요 여부")
	Boolean liked,

	@Schema(description = "좋아요한 멤버 식별자 목록")
	List<String> likedMembers
) {
}
