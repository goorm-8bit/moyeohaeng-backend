package eightbit.moyeohaeng.domain.selection.dto.response;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "장소 블록 좋아요 상태 응답")
public record PlaceBlockLikeSummaryResponse(
	@Schema(description = "총 좋아요 개수", example = "5")
	Long totalCount,

	@Schema(description = "요청한 사용자가 좋아요를 눌렀는지 여부", example = "true")
	boolean liked,

	@Schema(description = "좋아요를 누른 멤버 이메일 목록", example = "[\"test1@test.com\", \"test2@test.com\"]")
	List<String> likedMembers
) {
	/**
	 * PlaceBlockLikeSummaryResponse 생성 팩토리 메서드
	 *
	 * @param totalCount 총 좋아요 개수
	 * @param liked      요청한 사용자가 좋아요 눌렀는지 여부
	 * @param likedMembers 좋아요한 멤버 이메일 목록
	 * @return PlaceBlockLikeSummaryResponse
	 */
	public static PlaceBlockLikeSummaryResponse of(Long totalCount, boolean liked, List<String> likedMembers) {
		return new PlaceBlockLikeSummaryResponse(totalCount, liked, likedMembers);
	}
}
