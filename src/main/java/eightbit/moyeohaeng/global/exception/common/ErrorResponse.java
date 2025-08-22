package eightbit.moyeohaeng.global.exception.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ErrorResponse {
	private final int status;
	private final String code;
	private final String message;

	public static ErrorResponse of(ErrorCode errorCode) {
		return new ErrorResponse(errorCode.getStatus().value(), errorCode.getCode(), errorCode.getMessage());
	}

	public static ErrorResponse of(ErrorCode errorCode, String message) {
		return new ErrorResponse(errorCode.getStatus().value(), errorCode.getCode(), message);
	}
}
