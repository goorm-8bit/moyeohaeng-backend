package eightbit.moyeohaeng.domain.project.common.exception;

import org.springframework.http.HttpStatus;

import eightbit.moyeohaeng.global.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Project 도메인 관련 에러 코드 정의.
 */
@Getter
@RequiredArgsConstructor
public enum ProjectErrorCode implements ErrorCode {
	PROJECT_NOT_FOUND(HttpStatus.NOT_FOUND, "프로젝트를 찾을 수 없습니다. ID: %d", "PJ1001"),
	FORBIDDEN(HttpStatus.FORBIDDEN, "접근 권한이 없습니다.", "PJ1002");

	private final HttpStatus status;
	private final String message;
	private final String code;
}
