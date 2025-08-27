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

	private final Environment env;

	/**
	 * 로그인 요청 시 매개변수로 주어진 refreshToken을 쿠키를 생성하는 메서드
	 * @param refreshToken
	 * @return RefreshToken이 담긴 쿠키
	 */
	public ResponseCookie createRefreshTokenCookie(String refreshToken) {
		// 활성 프로필 확인 (local이면 secure=false, 그 외에는 true)
		String[] activeProfiles = env.getActiveProfiles();
		boolean isLocalProfile = Arrays.stream(activeProfiles)
				.anyMatch(profile -> "local".equals(profile));

		return ResponseCookie.from("refreshToken", refreshToken)
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
		// 활성 프로필 확인 (local이면 secure=false, 그 외에는 true)
		String[] activeProfiles = env.getActiveProfiles();
		boolean isLocalProfile = Arrays.stream(activeProfiles)
				.anyMatch(profile -> "local".equals(profile));

		return ResponseCookie.from("refreshToken", "")
			.httpOnly(true)
			.path("/")
			.secure(!isLocalProfile) // local 프로필이 아닌 경우에만 secure=true
			.maxAge(0)
			.sameSite("Lax")
			.build();
	}
}
