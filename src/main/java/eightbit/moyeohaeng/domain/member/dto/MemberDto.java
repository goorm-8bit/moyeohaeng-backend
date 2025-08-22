package eightbit.moyeohaeng.domain.member.dto;

import eightbit.moyeohaeng.domain.member.entity.member.Member;

public class MemberDto {

	/**
	 * 회원가입 요청 DTO
	 */
	public record RegisterRequest(
		String email,
		String password,
		String name
	) {
		public Member toEntity() {
			return Member.builder()
				.email(this.email)
				.password(this.password) // 비밀번호 암호화는 서비스 레이어에서 처리
				.name(this.name)
				.build();
		}
	}

	/**
	 * 회원 정보 수정 요청 DTO
	 */
	public record UpdateRequest(
		String name,
		String profileImage,
		String password
	) {
	}

	/**
	 * 회원 정보 응답 DTO
	 */
	public record Info(
		Long id,
		String email,
		String name,
		String profileImage
	) {
		public static Info from(Member member) {
			return new Info(
				member.getId(),
				member.getEmail(),
				member.getName(),
				member.getProfileImage()
			);
		}
	}
}
