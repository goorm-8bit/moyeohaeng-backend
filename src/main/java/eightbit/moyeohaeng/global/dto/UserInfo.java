package eightbit.moyeohaeng.global.dto;

import eightbit.moyeohaeng.domain.auth.UserRole;
import eightbit.moyeohaeng.domain.member.entity.member.Member;

public record UserInfo(
	Long id,

	// TODO: email을 반환하지 않고, 개인 정보와 관련 없는 식별자로 대체하기
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

	public static UserInfo guest(String identifier) {
		String suffix = identifier.substring(0, Math.min(identifier.length(), 4));
		return new UserInfo(
			null,
			identifier,
			"손님 " + suffix,
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
