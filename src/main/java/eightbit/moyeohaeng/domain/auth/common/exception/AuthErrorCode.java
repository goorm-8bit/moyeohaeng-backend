package eightbit.moyeohaeng.domain.auth.common.exception;

import org.springframework.http.HttpStatus;

import eightbit.moyeohaeng.global.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AuthErrorCode implements ErrorCode {

	DUPLICATE_EMAIL(HttpStatus.CONFLICT, "이미 존재하는 이메일입니다.", "A1001"),
	LOGIN_FAIL(HttpStatus.BAD_REQUEST, "이메일과 비밀번호가 일치하지 않습니다.", "A1002"),
	INVALID_CREDENTIALS(HttpStatus.BAD_REQUEST, "존재하지 않는 이메일 입니다.", "A1003"),
	// JWT 관련
	TOKEN_NOT_FOUND_IN_DB(HttpStatus.UNAUTHORIZED, "토큰이 존재하지 않습니다.", "A1101"),
	REFRESH_TOKEN_NOT_FOUND(HttpStatus.UNAUTHORIZED, "리프레시 토큰을 찾을 수 없습니다.", "A1107"),
	UNAUTHORIZED_ACCESS(HttpStatus.UNAUTHORIZED, "접근 권한이 없습니다.", "A1102"),
	MALFORMED_TOKEN(HttpStatus.BAD_REQUEST, "유효하지 않은 토큰 형식입니다.", "A1103"),
	EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "토큰이 만료되었습니다.", "A1104"),
	UNSUPPORTED_TOKEN(HttpStatus.BAD_REQUEST, "지원하지 않는 토큰 형식입니다.", "A1105"),
	INVALID_TOKEN_PARAMETER(HttpStatus.BAD_REQUEST, "잘못된 토큰 파라미터입니다.", "A1106");

	private final HttpStatus status;
	private final String message;
	private final String code;
}
