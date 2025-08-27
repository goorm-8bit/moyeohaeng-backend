package eightbit.moyeohaeng.domain.auth.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import eightbit.moyeohaeng.domain.auth.common.exception.AuthErrorCode;
import eightbit.moyeohaeng.domain.auth.common.exception.AuthException;
import eightbit.moyeohaeng.domain.auth.common.success.AuthSuccessCode;
import eightbit.moyeohaeng.domain.auth.controller.swagger.AuthApi;
import eightbit.moyeohaeng.domain.auth.dto.TokenResult;
import eightbit.moyeohaeng.domain.auth.dto.request.LoginRequest;
import eightbit.moyeohaeng.domain.auth.dto.request.SignUpRequest;
import eightbit.moyeohaeng.domain.auth.service.AuthService;
import eightbit.moyeohaeng.domain.auth.utils.CookieGenerator;
import eightbit.moyeohaeng.global.success.SuccessResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * 인증 관련 API를 처리하는 컨트롤러
 *
 * 표준화된 응답 형식을 사용하는 컨트롤러 예시입니다:
 * 1. 회원가입(void 반환): SuccessResponse.from(SuccessCode) 사용하여 데이터 없는 응답 생성
 * 2. 로그인(데이터 반환): SuccessResponse.of(SuccessCode, data) 사용하여 데이터와 함께 응답 생성
 *
 * GlobalSuccessResponseAdvice 클래스는 이 응답들을 자동으로 표준화하므로,
 * 모든 API 응답은 동일한 구조를 갖게 됩니다: { "status": int, "message": String, "data": Object }
 */
@RestController
@RequestMapping("/v1/auth")
@RequiredArgsConstructor
public class AuthController implements AuthApi {

	private final AuthService authService;
	private final CookieGenerator cookieGenerator;

	/**
	 * 회원 가입 API
	 * 데이터 없이 성공 코드만 반환하는 예시
	 *
	 * @param signUpRequest 회원 가입 요청 정보
	 * @return 생성 성공 응답 (201 Created)
	 */
	@PostMapping("/signup")
	public SuccessResponse<Void> signUp(@Valid @RequestBody SignUpRequest signUpRequest) {
		authService.signUp(signUpRequest);
		return SuccessResponse.from(AuthSuccessCode.SIGNUP_SUCCESS); // 생성 성공 (201) 응답
	}

	/**
	 * 로그인 API
	 * 데이터를 포함한 성공 응답 예시
	 *
	 * @param loginRequest 로그인 요청 정보
	 * @return accessToken은 body, refreshToken은 http only 포함한 성공 응답 (200 OK)
	 */
	@PostMapping("/login")
	public ResponseEntity<SuccessResponse<String>> login(@Valid @RequestBody LoginRequest loginRequest) {
		TokenResult tokenResult = authService.login(loginRequest);
		ResponseCookie responseCookie = cookieGenerator.createRefreshTokenCookie(tokenResult.refreshToken());

		return ResponseEntity.ok()
			.header(HttpHeaders.SET_COOKIE, responseCookie.toString())
			.body(SuccessResponse.of(AuthSuccessCode.LOGIN_SUCCESS, tokenResult.accessToken()));
	}

	@PostMapping("/refresh")
	public SuccessResponse<String> refreshAccessToken(HttpServletRequest request) {
		// HTTP-only 쿠키에서 리프레시 토큰 추출
		String refreshToken = null;
		Cookie[] cookies = request.getCookies();

		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if ("refreshToken".equals(cookie.getName())) {
					refreshToken = cookie.getValue();
					break;
				}
			}
		}

		if (refreshToken == null) {
			throw new AuthException(AuthErrorCode.REFRESH_TOKEN_NOT_FOUND);
		}

		// 액세스 토큰 재발급
		String accessToken = authService.reissueToken(refreshToken);

		return SuccessResponse.of(AuthSuccessCode.TOKEN_REFRESH_SUCCESS, accessToken);
	}

}
