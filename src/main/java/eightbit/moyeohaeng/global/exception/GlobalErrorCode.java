package eightbit.moyeohaeng.global.exception;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum GlobalErrorCode implements ErrorCode {

	// 4xx
	RESOURCE_NOT_FOUND(HttpStatus.NOT_FOUND, "리소스를 찾을 수 없습니다.", "G1001"),
	METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "지원하지 않는 메소드입니다.", "G1002"),
	MISSING_HEADER(HttpStatus.BAD_REQUEST, "요청에 필요한 헤더가 존재하지 않습니다.", "G1003"),
	FORBIDDEN(HttpStatus.FORBIDDEN, "접근이 금지되었습니다.", "G1004"),
	INVALID_PATH_VARIABLE_FORMAT(HttpStatus.BAD_REQUEST, "요청 경로 파라미터의 형식이 올바르지 않습니다.", "G1005"),
	INVALID_INPUT(HttpStatus.BAD_REQUEST, "입력값이 올바르지 않습니다.", "G1006"),
	INVALID_DATA_ACCESS_API_USAGE(HttpStatus.BAD_REQUEST, "잘못된 데이터 접근 API 호출입니다.", "G1007"),

	// 5xx
	INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류가 발생했습니다.", "G1007");

	private final HttpStatus status;
	private final String message;
	private final String code;

	@Override
	public HttpStatus getStatus() {
		return status;
	}

	@Override
	public String getMessage() {
		return message;
	}

	@Override
	public String getCode() {
		return code;
	}
}
