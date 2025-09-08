package eightbit.moyeohaeng.domain.selection.dto.response;

import com.querydsl.core.annotations.QueryProjection;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "마지막 댓글")
public record PlaceBlockLastComment(
	@Schema(description = "내용", example = "재밌을 것 같아")
	String content,

	@Schema(description = "작성자", example = "test@test.com")
	String author
) {
	@QueryProjection
	public PlaceBlockLastComment {
	}

	public static PlaceBlockLastComment empty() {
		return new PlaceBlockLastComment(null, null);
	}
}
