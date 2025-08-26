package eightbit.moyeohaeng.domain.member.exception;

import org.springframework.http.HttpStatus;

import eightbit.moyeohaeng.global.success.SuccessCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 회원 도메인에서 사용되는 성공 코드
 */
@Getter
@RequiredArgsConstructor
public enum MemberSuccessCode implements SuccessCode {

	/**
	 * 200 OK
	 */
	PROFILE_READ_SUCCESS(HttpStatus.OK, "프로필 조회 성공"),
	PROFILE_UPDATE_SUCCESS(HttpStatus.OK, "프로필 수정 성공"),
	MEMBER_DELETE_SUCCESS(HttpStatus.OK, "회원 탈퇴 성공"),

	/**
	 * 201 Created
	 */
	PROFILE_CREATE_SUCCESS(HttpStatus.CREATED, "프로필 생성 성공");

	private final HttpStatus status;
	private final String message;
}
