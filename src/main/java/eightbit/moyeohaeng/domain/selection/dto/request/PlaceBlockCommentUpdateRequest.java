package eightbit.moyeohaeng.domain.selection.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "댓글 수정 요청 DTO")
public record PlaceBlockCommentUpdateRequest(
	@Schema(description = "수정 댓글 내용", example = "수정된 댓글입니다.")
	@NotBlank
	@Size(max = 500)
	String content
) {
}
