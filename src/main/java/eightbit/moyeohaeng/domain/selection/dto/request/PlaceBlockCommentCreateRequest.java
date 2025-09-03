package eightbit.moyeohaeng.domain.selection.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "댓글 생성 요청 DTO")
public record PlaceBlockCommentCreateRequest(
	@Schema(description = "댓글 내용", example = "댓글입니다.")
	@NotBlank
	@Size(max = 500)
	String content
) {
}
