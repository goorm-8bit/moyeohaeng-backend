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
	TEAM_NOT_FOUND(HttpStatus.NOT_FOUND, "팀을 찾을 수 없습니다.", "P1003"),
	NOT_PROJECT_OWNER(HttpStatus.FORBIDDEN, "프로젝트 소유자만 수정할 수 있습니다.", "P1004"),

	// 공유 기능
	SHARE_NOT_ALLOWED(HttpStatus.FORBIDDEN, "해당 프로젝트는 공유가 허용되지 않았습니다.", "P1101");

	private final HttpStatus status;
	private final String message;
	private final String code;
}
