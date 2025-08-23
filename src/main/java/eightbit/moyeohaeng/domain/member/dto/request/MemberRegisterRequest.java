package eightbit.moyeohaeng.domain.member.dto.request;

import eightbit.moyeohaeng.domain.member.entity.member.Member;

public record MemberRegisterRequest(
	String email,
	String password,
	String name
) {
	public Member toEntity() {
		return Member.builder()
			.email(this.email)
			.password(this.password) // Password encoding is handled in the service layer
			.name(this.name)
			.build();
	}
}
