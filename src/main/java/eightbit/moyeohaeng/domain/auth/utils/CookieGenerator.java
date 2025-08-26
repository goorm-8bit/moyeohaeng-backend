package eightbit.moyeohaeng.domain.auth.utils;

import java.time.Duration;

import org.springframework.http.ResponseCookie;

/**
 * 응답할 때 보내야 할 쿠키를 만드는 정적 유틸리티 클래스
 */
public class CookieGenerator {

	/**
	 * 로그인 요청 시 매개변수로 주어진 refreshToken을 쿠키를 생성하는 메서드
	 * @param refreshToken
	 * @return RefreshToken이 담긴 쿠키
	 */
	public static ResponseCookie createRefreshTokenCookie(String refreshToken) {

		return ResponseCookie.from("refreshToken", refreshToken)
			.httpOnly(true)
			.path("/")
			.maxAge(Duration.ofDays(7))
			.sameSite("Lax")
			.build();
	}

	/**
	 * 로그아웃 요청 시 브라우저 측에 존재하는 refreshToken 쿠키의 시간을 변경해 무력화시키는 메서드
	 * @return 만료 시간이 0으로 변경된 쿠키
	 */
	public static ResponseCookie destroyRefreshTokenCookie() {

		return ResponseCookie.from("refreshToken", "")
			.httpOnly(true)
			.path("/")
			.maxAge(0)
			.build();
	}
}
