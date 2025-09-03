package eightbit.moyeohaeng.domain.selection.common.exception;

import org.springframework.http.HttpStatus;

import eightbit.moyeohaeng.global.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PlaceGroupErrorCode implements ErrorCode {

	;

	private final HttpStatus status;
	private final String message;
	private final String code;
}
