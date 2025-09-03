package eightbit.moyeohaeng.domain.selection.dto.response;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "장소 블록 댓글 응답 DTO")
public record PlaceBlockCommentResponse(

	@Schema(description = "댓글 ID", example = "10")
	Long id,

	@Schema(description = "내용", example = "댓글입니다.")
	String content,

	@Schema(description = "작성자 이름", example = "홍길동")
	String name,

	@Schema(description = "작성자 프로필 이미지 URL", example = "https://example.com/profile.png")
	String profileImage,

	@Schema(description = "작성 시각")
	LocalDateTime createdAt,

	@Schema(description = "수정 시각")
	LocalDateTime modifiedAt
) {
}
