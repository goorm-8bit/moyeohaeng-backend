package eightbit.moyeohaeng.global.dto;

import eightbit.moyeohaeng.domain.member.entity.member.Member;
import eightbit.moyeohaeng.global.security.UserType;

public record UserInfo(
	Long id,
	String email,
	String name,
	String profileImage,
	UserType userType
) {
	public static UserInfo member(Member member) {
		return new UserInfo(
			member.getId(),
			member.getEmail(),
			member.getName(),
			member.getProfileImage(),
			UserType.MEMBER
		);
	}

	public static UserInfo guest(UserType userType) {
		return new UserInfo(
			null,
			null,
			null,
			null,
			userType
		);
	}
}
