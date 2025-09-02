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
	PROJECT_NOT_FOUND(HttpStatus.NOT_FOUND, "프로젝트를 찾을 수 없습니다. ID: %d", "P1001"),
	FORBIDDEN(HttpStatus.FORBIDDEN, "접근 권한이 없습니다.", "P1002"),

	// 공유 기능
	CREATE_SHARE_TOKEN_FAIL(HttpStatus.FORBIDDEN, "공유 토큰 생성에 실패하였습니다. (Owner만 가능합니다.)", "P1101"),
	SHARE_NOT_ALLOWED(HttpStatus.FORBIDDEN, "해당 프로젝트는 공유가 허용되지 않았습니다.", "P1102");

	private final HttpStatus status;
	private final String message;
	private final String code;
}
