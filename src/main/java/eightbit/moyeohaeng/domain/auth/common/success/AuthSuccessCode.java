package eightbit.moyeohaeng.domain.auth.common.success;

import org.springframework.http.HttpStatus;

import eightbit.moyeohaeng.global.success.SuccessCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 인증 도메인에서 사용되는 성공 코드
 */
@Getter
@RequiredArgsConstructor
public enum AuthSuccessCode implements SuccessCode {

	/**
	 * 200 OK
	 */
	LOGIN_SUCCESS(HttpStatus.OK, "로그인 성공"),
	LOGOUT_SUCCESS(HttpStatus.OK, "로그아웃 성공"),
	TOKEN_REFRESH_SUCCESS(HttpStatus.OK, "토큰 갱신 성공"),

	SIGNUP_SUCCESS(HttpStatus.CREATED, "회원가입 성공");

	private final HttpStatus status;
	private final String message;
}
