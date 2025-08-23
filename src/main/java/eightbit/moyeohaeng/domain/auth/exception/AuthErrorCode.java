package eightbit.moyeohaeng.domain.auth.exception;

import org.springframework.http.HttpStatus;

import eightbit.moyeohaeng.global.exception.common.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AuthErrorCode implements ErrorCode {

	DUPLICATE_EMAIL(HttpStatus.CONFLICT, "이미 존재하는 이메일입니다.", "A1001");

	private final HttpStatus status;
	private final String message;
	private final String code;
}
