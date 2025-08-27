package eightbit.moyeohaeng.domain.auth.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 *
 * 인증 토큰 페어를 전달하는 내부 DTO.
 * 외부 응답에는 accessToken만 사용하고, refreshToken은 HttpOnly 쿠키로 발급합니다.
 *  @param accessToken 접근 토큰(응답 본문에 포함 가능)
 *  @param refreshToken 리프레시 토큰(직렬화 제외)
 * */
public record TokenResult(
	String accessToken,

	@JsonIgnore
	String refreshToken
) {

	public static TokenResult of(String accessToken, String refreshToken) {
		return new TokenResult(accessToken, refreshToken);
	}
}
