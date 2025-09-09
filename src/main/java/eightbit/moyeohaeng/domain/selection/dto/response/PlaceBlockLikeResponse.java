package eightbit.moyeohaeng.domain.selection.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "장소 블록 좋아요 응답 DTO")
public record PlaceBlockLikeResponse(

	@Schema(description = "장소 블록 ID", example = "10")
	Long placeBlockId,

	@Schema(description = "작성자", example = "user@example.com")
	String author,

	@Schema(description = "좋아요 여부 (true = 좋아요, false = 좋아요 취소)", example = "true")
	boolean liked
) {
	public static PlaceBlockLikeResponse of(Long placeBlockId, String author, boolean liked) {
		return new PlaceBlockLikeResponse(placeBlockId, author, liked);
	}
}
