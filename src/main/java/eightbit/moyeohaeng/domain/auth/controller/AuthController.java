package eightbit.moyeohaeng.domain.auth.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import eightbit.moyeohaeng.domain.auth.dto.request.LoginRequest;
import eightbit.moyeohaeng.domain.auth.dto.request.SignUpRequest;
import eightbit.moyeohaeng.domain.auth.dto.response.TokenResponse;
import eightbit.moyeohaeng.domain.auth.exception.AuthSuccessCode;
import eightbit.moyeohaeng.domain.auth.service.AuthService;
import eightbit.moyeohaeng.global.success.SuccessResponse;
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
public class AuthController {

	private final AuthService authService;

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
	 * @return 토큰 정보를 포함한 성공 응답 (200 OK)
	 */
	@PostMapping("/login")
	public SuccessResponse<TokenResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
		TokenResponse tokenResponse = authService.login(loginRequest);
		return SuccessResponse.of(AuthSuccessCode.LOGIN_SUCCESS, tokenResponse); // 로그인 성공 (200) 응답
	}

}
