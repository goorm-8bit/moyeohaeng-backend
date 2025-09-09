package eightbit.moyeohaeng.domain.selection.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "장소 블록 댓글 요약 DTO")
public record PlaceBlockCommentSummary(
	@Schema(description = "댓글 개수", example = "1")
	Long totalCount,

	@Schema(description = "마지막 댓글", nullable = true)
	PlaceBlockLastComment lastComment
) {
	public static PlaceBlockCommentSummary of(Long totalCount, PlaceBlockLastComment lastComment) {
		return new PlaceBlockCommentSummary(totalCount, lastComment);
	}

	public static PlaceBlockCommentSummary empty() {
		return new PlaceBlockCommentSummary(0L, PlaceBlockLastComment.empty());
	}
}
