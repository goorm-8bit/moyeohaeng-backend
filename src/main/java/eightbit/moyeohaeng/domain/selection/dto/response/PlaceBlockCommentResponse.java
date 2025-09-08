package eightbit.moyeohaeng.domain.selection.dto.response;

import java.time.LocalDateTime;

import eightbit.moyeohaeng.domain.member.common.exception.MemberErrorCode;
import eightbit.moyeohaeng.domain.member.common.exception.MemberException;
import eightbit.moyeohaeng.domain.member.entity.member.Member;
import eightbit.moyeohaeng.domain.member.repository.MemberRepository;
import eightbit.moyeohaeng.domain.selection.entity.PlaceBlockComment;
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
	public static PlaceBlockCommentResponse of(PlaceBlockComment comment, MemberRepository memberRepository) {
		// author(email) 기반으로 Member 찾아오기
		Member member = memberRepository.findByEmail(comment.getAuthor())
			.orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));

		return new PlaceBlockCommentResponse(
			comment.getId(),
			comment.getContent(),
			member.getName(),
			member.getProfileImage(),
			comment.getCreatedAt(),
			comment.getModifiedAt()
		);
	}
}
