package eightbit.moyeohaeng.global.dto;

import eightbit.moyeohaeng.domain.auth.UserRole;
import eightbit.moyeohaeng.domain.member.entity.member.Member;

public record UserInfo(
	Long id,
	String email,
	String name,
	String profileImage,
	UserRole userRole
) {
	private UserInfo(Long id, String email, String name, String profileImage) {
		this(id, email, name, profileImage, null);
	}

	public static UserInfo member(Member member) {
		return new UserInfo(
			member.getId(),
			member.getEmail(),
			member.getName(),
			member.getProfileImage()
		);
	}

	public static UserInfo guest(String username) {
		return new UserInfo(
			null,
			username,
			"손님",
			null
		);
	}

	public UserInfo withRole(UserRole userRole) {
		return new UserInfo(
			this.id,
			this.email,
			this.name,
			this.profileImage,
			userRole
		);
	}
}
