package eightbit.moyeohaeng.domain.auth.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import eightbit.moyeohaeng.domain.auth.dto.request.LoginRequest;
import eightbit.moyeohaeng.domain.auth.dto.request.SignUpRequest;
import eightbit.moyeohaeng.domain.auth.dto.response.TokenResponse;
import eightbit.moyeohaeng.domain.auth.exception.AuthErrorCode;
import eightbit.moyeohaeng.domain.auth.exception.AuthException;
import eightbit.moyeohaeng.domain.member.entity.member.Member;
import eightbit.moyeohaeng.domain.member.repository.MemberRepository;
import eightbit.moyeohaeng.global.domain.auth.JwtTokenProvider;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

	private final MemberRepository memberRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtTokenProvider jwtTokenProvider;

	@Transactional
	public void signUp(SignUpRequest signUpRequest) {
		if (memberRepository.existsByEmail(signUpRequest.email())) {
			throw new AuthException(AuthErrorCode.DUPLICATE_EMAIL);
		}

		Member member = Member.builder()
			.email(signUpRequest.email())
			.password(passwordEncoder.encode(signUpRequest.password()))
			.name(signUpRequest.name())
			.build();

		memberRepository.save(member);
	}

	public TokenResponse login(LoginRequest loginRequest) {
		Member member = memberRepository.findByEmail(loginRequest.email())
			.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 이메일입니다."));

		if (!passwordEncoder.matches(loginRequest.password(), member.getPassword())) {
			throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
		}

		String accessToken = jwtTokenProvider.createAccessToken(String.valueOf(member.getId()));
		String refreshToken = jwtTokenProvider.createRefreshToken(String.valueOf(member.getId()));

		return new TokenResponse(accessToken, refreshToken);
	}

}
