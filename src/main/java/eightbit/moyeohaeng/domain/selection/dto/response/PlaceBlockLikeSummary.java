package eightbit.moyeohaeng.domain.selection.dto.response;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "장소 블록 좋아요 요약 DTO")
public record PlaceBlockLikeSummary(
	@Schema(description = "좋아요 개수", example = "2")
	int totalCount,

	@Schema(description = "요청한 사용자 좋아요 여부", example = "true")
	boolean liked,

	@Schema(description = "좋아요를 누른 사용자 목록", example = "[test@test.com, test2@test.com]")
	List<String> likedMembers
) {
	public static PlaceBlockLikeSummary of(int totalCount, boolean liked, List<String> likedMembers) {
		return new PlaceBlockLikeSummary(totalCount, liked, likedMembers);
	}

	public static PlaceBlockLikeSummary empty() {
		return new PlaceBlockLikeSummary(0, false, List.of());
	}
}
