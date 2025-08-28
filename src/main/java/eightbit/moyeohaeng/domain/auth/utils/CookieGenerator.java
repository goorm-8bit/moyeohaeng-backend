package eightbit.moyeohaeng.domain.auth.utils;

import java.time.Duration;

import org.springframework.core.env.Environment;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

/**
 * 응답할 때 보내야 할 쿠키를 만드는 유틸리티 컴포넌트
 */
@Component
@RequiredArgsConstructor
public class CookieGenerator {

	private static final String REFRESH_COOKIE = "refreshToken";
	private final Environment env;

	/**
	 * 로그인 요청 시 매개변수로 주어진 refreshToken을 쿠키를 생성하는 메서드
	 * @param refreshToken
	 * @return RefreshToken이 담긴 쿠키
	 */
	public ResponseCookie createRefreshTokenCookie(String refreshToken) {

		return ResponseCookie.from(REFRESH_COOKIE, refreshToken)
			.httpOnly(true)
			.path("/")
			.secure(true) // TODO: 향후 개발시 내부 로직 변경
			.maxAge(Duration.ofDays(7))
			.sameSite("Lax")
			.build();
	}

	/**
	 * 로그아웃 요청 시 브라우저 측에 존재하는 refreshToken 쿠키의 시간을 변경해 무력화시키는 메서드
	 * @return 만료 시간이 0으로 변경된 쿠키
	 */
	public ResponseCookie destroyRefreshTokenCookie() {

		return ResponseCookie.from(REFRESH_COOKIE, "")
			.httpOnly(true)
			.path("/")
			.secure(false) // TODO: 향후 개발시 내부 로직 변경
			.maxAge(0)
			.sameSite("Lax")
			.build();
	}

}
