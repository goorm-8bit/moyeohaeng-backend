package eightbit.moyeohaeng.domain.member.dto.response;

import eightbit.moyeohaeng.domain.member.entity.member.Member;

public record MemberInfoResponse(
	Long id,
	String email,
	String name,
	String profileImage
) {
	public static MemberInfoResponse from(Member member) {
		return new MemberInfoResponse(
			member.getId(),
			member.getEmail(),
			member.getName(),
			member.getProfileImage()
		);
	}
}