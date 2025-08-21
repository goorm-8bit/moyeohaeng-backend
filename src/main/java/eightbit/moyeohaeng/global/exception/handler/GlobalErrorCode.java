package eightbit.moyeohaeng.global.exception.handler;

import org.springframework.http.HttpStatus;

import eightbit.moyeohaeng.global.exception.common.ErrorCode;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum GlobalErrorCode implements ErrorCode {
	RESOURCE_NOT_FOUND(HttpStatus.NOT_FOUND, "리소스를 찾을 수 없습니다.", "G4001"),
	METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "지원하지 않는 메소드입니다.", "G4002"),
	INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류가 발생했습니다.", "G5001"),
	MISSING_HEADER(HttpStatus.BAD_REQUEST, "요청에 필요한 헤더가 존재하지 않습니다.", "G4003"),
	FORBIDDEN(HttpStatus.FORBIDDEN, "접근이 금지되었습니다.", "G4004"),
	INVALID_PATH_VARIABLE_FORMAT(HttpStatus.BAD_REQUEST, "요청 경로 파라미터의 형식이 올바르지 않습니다.", "G4005"),
	INVALID_INPUT(HttpStatus.BAD_REQUEST, "입력값이 올바르지 않습니다.", "G4006");

	public static final String PREFIX = "[GLOBAL ERROR] ";

	private final HttpStatus status;
	private final String rawMessage;
	private final String code;

	@Override
	public HttpStatus getStatus() {
		return status;
	}

	@Override
	public String getMessage() {
		return PREFIX + rawMessage;
	}
	
	@Override
	public String getCode() {
		return code;
	}
}
