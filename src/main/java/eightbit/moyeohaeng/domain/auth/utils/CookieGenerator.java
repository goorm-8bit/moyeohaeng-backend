package eightbit.moyeohaeng.domain.auth.utils;

import java.time.Duration;
import java.util.Arrays;

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
		boolean isLocalProfile = bIsLocalProfile();

		return ResponseCookie.from(REFRESH_COOKIE, refreshToken)
			.httpOnly(true)
			.path("/")
			.secure(!isLocalProfile) // local 프로필이 아닌 경우에만 secure=true
			.maxAge(Duration.ofDays(7))
			.sameSite("Lax")
			.build();
	}

	/**
	 * 로그아웃 요청 시 브라우저 측에 존재하는 refreshToken 쿠키의 시간을 변경해 무력화시키는 메서드
	 * @return 만료 시간이 0으로 변경된 쿠키
	 */
	public ResponseCookie destroyRefreshTokenCookie() {
		boolean isLocalProfile = bIsLocalProfile();

		return ResponseCookie.from(REFRESH_COOKIE, "")
			.httpOnly(true)
			.path("/")
			.secure(!isLocalProfile) // local 프로필이 아닌 경우에만 secure=true
			.maxAge(0)
			.sameSite("Lax")
			.build();
	}

	/**
	 * 현재 프로필이 로컬 환경인지 확인
	 * @return 로컬 환경이면 true, 그 외에는 false
	 */
	private boolean bIsLocalProfile() {
		String[] activeProfiles = env.getActiveProfiles();
		return Arrays.stream(activeProfiles)
			.anyMatch(profile -> "local".equals(profile));
	}
}
