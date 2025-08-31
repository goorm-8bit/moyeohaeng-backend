package eightbit.moyeohaeng.domain.project.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "장소 블록 댓글 요약 응답")
public record PlaceBlockCommentSummaryResponse(
	@Schema(description = "총 댓글 수")
	Integer totalCount,

	@Schema(description = "마지막 댓글 미리보기", nullable = true)
	LastComment lastComment
) {
	@Schema(description = "마지막 댓글 정보")
	public record LastComment(
		@Schema(description = "내용")
		String content,

		@Schema(description = "작성자 식별자")
		String author
	) {
	}
}
