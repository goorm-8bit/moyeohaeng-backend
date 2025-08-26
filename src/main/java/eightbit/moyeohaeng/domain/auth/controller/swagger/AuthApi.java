package eightbit.moyeohaeng.domain.auth.controller.swagger;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

import eightbit.moyeohaeng.domain.auth.dto.request.LoginRequest;
import eightbit.moyeohaeng.domain.auth.dto.request.SignUpRequest;
import eightbit.moyeohaeng.global.success.SuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@Tag(name = "인증 API", description = "회원가입, 로그인, 토큰 갱신 등 인증 관련 API")
public interface AuthApi {

	@Operation(
		summary = "회원 가입",
		description = "사용자 정보를 받아 회원 가입을 처리합니다."
	)
	@ApiResponses({
		@ApiResponse(
			responseCode = "201",
			description = "회원가입 성공",
			content = @Content(schema = @Schema(implementation = SuccessResponse.class))
		),
		@ApiResponse(
			responseCode = "409",
			description = "이미 존재하는 이메일",
			content = @Content
		)
	})
	SuccessResponse<Void> signUp(
		@Parameter(description = "회원 가입 요청 정보", required = true)
		@Valid @RequestBody SignUpRequest signUpRequest
	);

	@Operation(
		summary = "로그인",
		description = "이메일과 비밀번호로 로그인하고 토큰을 발급받습니다. accessToken은 응답 본문에 제공되며, refreshToken은 HTTP-only 쿠키로 전송됩니다."
	)
	@ApiResponses({
		@ApiResponse(
			responseCode = "200",
			description = "로그인 성공",
			content = @Content(schema = @Schema(implementation = SuccessResponse.class))
		),
		@ApiResponse(
			responseCode = "400",
			description = "이메일 또는 비밀번호가 일치하지 않음",
			content = @Content
		)
	})
	ResponseEntity<SuccessResponse<String>> login(
		@Parameter(description = "로그인 요청 정보", required = true)
		@Valid @RequestBody LoginRequest loginRequest
	);

	@Operation(
		summary = "토큰 갱신",
		description = "HTTP-only 쿠키에 저장된 리프레시 토큰을 사용하여 새로운 액세스 토큰을 발급받습니다."
	)
	@ApiResponses({
		@ApiResponse(
			responseCode = "200",
			description = "토큰 갱신 성공",
			content = @Content(schema = @Schema(implementation = SuccessResponse.class))
		),
		@ApiResponse(
			responseCode = "401",
			description = "리프레시 토큰이 유효하지 않거나 만료됨",
			content = @Content
		)
	})
	SuccessResponse<String> refreshAccessToken(
		HttpServletRequest request
	);
}
