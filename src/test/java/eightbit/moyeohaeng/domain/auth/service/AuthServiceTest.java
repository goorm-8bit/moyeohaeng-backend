package eightbit.moyeohaeng.domain.auth.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import eightbit.moyeohaeng.domain.auth.common.exception.AuthErrorCode;
import eightbit.moyeohaeng.domain.auth.common.exception.AuthException;
import eightbit.moyeohaeng.domain.auth.dto.TokenResult;
import eightbit.moyeohaeng.domain.auth.dto.request.LoginRequest;
import eightbit.moyeohaeng.domain.auth.dto.request.SignUpRequest;
import eightbit.moyeohaeng.domain.member.entity.member.Member;
import eightbit.moyeohaeng.domain.member.repository.MemberRepository;
import eightbit.moyeohaeng.global.domain.auth.JwtTokenProvider;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

	@InjectMocks
	private AuthService authService;

	@Mock
	private MemberRepository memberRepository;

	@Mock
	private PasswordEncoder passwordEncoder;

	@Mock
	private JwtTokenProvider jwtTokenProvider;

	@DisplayName("회원가입 테스트 - 성공")
	@Test
	void signup_success() {
		// given
		String email = "test@eightbit.com";
		SignUpRequest request = SignUpRequest.of(email, "@Test12345", "테스트1");
		given(memberRepository.existsByEmail(email)).willReturn(false);

		// when & then
		assertThatCode(() -> authService.signUp(request))
			.doesNotThrowAnyException();
		verify(memberRepository).save(any(Member.class));
	}

	@DisplayName("회원가입 테스트 - 실패 (이메일 중복)")
	@Test
	void signup_fail_duplicateEmail() {
		// given
		String email = "test@eightbit.com";
		SignUpRequest request = SignUpRequest.of(email, "@Test12345", "테스트1");
		given(memberRepository.existsByEmail(email)).willReturn(true);

		// when & then
		assertThatThrownBy(() -> authService.signUp(request))
			.isInstanceOf(AuthException.class)
			.hasMessage(AuthErrorCode.DUPLICATE_EMAIL.getMessage());
	}

	@DisplayName("로그인 테스트 - 성공")
	@Test
	void login_success() {
		// given
		String email = "test@eightbit.com";
		String password = "@Test12345";
		LoginRequest request = new LoginRequest(email, password);
		
		Member member = Member.builder()
			.id(1L)
			.email(email)
			.password("encodedPassword")
			.name("테스트1")
			.build();
		
		String accessToken = "access.token.test";
		String refreshToken = "refresh.token.test";
		
		given(memberRepository.findByEmail(email)).willReturn(Optional.of(member));
		given(passwordEncoder.matches(password, member.getPassword())).willReturn(true);
		given(jwtTokenProvider.createAccessToken(String.valueOf(member.getId()))).willReturn(accessToken);
		given(jwtTokenProvider.createRefreshToken(String.valueOf(member.getId()))).willReturn(refreshToken);
		
		// when
		TokenResult result = authService.login(request);
		
		// then
		assertThat(result.accessToken()).isEqualTo(accessToken);
		assertThat(result.refreshToken()).isEqualTo(refreshToken);
	}

	@DisplayName("로그인 테스트 - 실패 (존재하지 않는 이메일)")
	@Test
	void login_fail_emailNotFound() {
		// given
		String email = "nonexistent@eightbit.com";
		String password = "@Test12345";
		LoginRequest request = new LoginRequest(email, password);
		
		given(memberRepository.findByEmail(email)).willReturn(Optional.empty());
		
		// when & then
		assertThatThrownBy(() -> authService.login(request))
			.isInstanceOf(AuthException.class)
			.hasMessage(AuthErrorCode.LOGIN_FAIL.getMessage());
	}

	@DisplayName("로그인 테스트 - 실패 (비밀번호 불일치)")
	@Test
	void login_fail_passwordMismatch() {
		// given
		String email = "test@eightbit.com";
		String password = "wrongPassword";
		LoginRequest request = new LoginRequest(email, password);
		
		Member member = Member.builder()
			.id(1L)
			.email(email)
			.password("encodedPassword")
			.name("테스트1")
			.build();
		
		given(memberRepository.findByEmail(email)).willReturn(Optional.of(member));
		given(passwordEncoder.matches(password, member.getPassword())).willReturn(false);
		
		// when & then
		assertThatThrownBy(() -> authService.login(request))
			.isInstanceOf(AuthException.class)
			.hasMessage(AuthErrorCode.LOGIN_FAIL.getMessage());
	}

	@DisplayName("토큰 재발급 테스트 - 성공")
	@Test
	void reissueToken_success() {
		// given
		String refreshToken = "valid.refresh.token";
		String newAccessToken = "new.access.token";
		
		given(jwtTokenProvider.reissueAccessToken(refreshToken)).willReturn(newAccessToken);
		
		// when
		String result = authService.reissueToken(refreshToken);
		
		// then
		assertThat(result).isEqualTo(newAccessToken);
	}

	@DisplayName("토큰 재발급 테스트 - 실패 (리프레시 토큰 만료)")
	@Test
	void reissueToken_fail_tokenExpired() {
		// given
		String expiredToken = "expired.refresh.token";
		
		given(jwtTokenProvider.reissueAccessToken(expiredToken)).willThrow(ExpiredJwtException.class);
		
		// when & then
		assertThatThrownBy(() -> authService.reissueToken(expiredToken))
			.isInstanceOf(AuthException.class)
			.hasMessage(AuthErrorCode.REFRESH_TOKEN_EXPIRED.getMessage());
	}

	@DisplayName("토큰 재발급 테스트 - 실패 (유효하지 않은 리프레시 토큰)")
	@Test
	void reissueToken_fail_invalidToken() {
		// given
		String invalidToken = "invalid.refresh.token";
		
		given(jwtTokenProvider.reissueAccessToken(invalidToken)).willThrow(JwtException.class);
		
		// when & then
		assertThatThrownBy(() -> authService.reissueToken(invalidToken))
			.isInstanceOf(AuthException.class)
			.hasMessage(AuthErrorCode.INVALID_REFRESH_TOKEN.getMessage());
	}
}
