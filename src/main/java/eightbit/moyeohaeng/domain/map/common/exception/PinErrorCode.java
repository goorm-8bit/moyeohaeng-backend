package eightbit.moyeohaeng.domain.map.common.exception;

import org.springframework.http.HttpStatus;

import eightbit.moyeohaeng.global.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PinErrorCode implements ErrorCode {

	PIN_NOT_FOUND(HttpStatus.NOT_FOUND, "핀을 찾을 수 없습니다.", "P1001"),
	DUPLICATE_PIN(HttpStatus.CONFLICT, "이미 존재하는 핀입니다.", "P1002");

	private final HttpStatus status;
	private final String message;
	private final String code;
}
