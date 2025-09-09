package eightbit.moyeohaeng.domain.selection.dto.response;

import eightbit.moyeohaeng.domain.selection.entity.PlaceBlockComment;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "장소 블록 댓글 삭제 응답 DTO")
public record PlaceBlockCommentDeleteResponse(

	@Schema(description = "장소 블록 ID", example = "10")
	Long placeBlockId,

	@Schema(description = "댓글 ID", example = "100")
	Long commentId
) {
	public static PlaceBlockCommentDeleteResponse from(PlaceBlockComment comment) {
		return new PlaceBlockCommentDeleteResponse(
			comment.getPlaceBlock().getId(),
			comment.getId()
		);
	}
}
