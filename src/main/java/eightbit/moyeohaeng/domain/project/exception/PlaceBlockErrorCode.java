package eightbit.moyeohaeng.domain.project.exception;

import org.springframework.http.HttpStatus;

import eightbit.moyeohaeng.global.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * PlaceBlock 도메인 관련 에러 코드 정의.
 */
@Getter
@RequiredArgsConstructor
public enum PlaceBlockErrorCode implements ErrorCode {
	PLACE_BLOCK_NOT_FOUND(HttpStatus.NOT_FOUND, "장소 블록을 찾을 수 없습니다.", "PB1001"),
	FORBIDDEN(HttpStatus.FORBIDDEN, "접근 권한이 없습니다.", "PB1002"),
	LIMIT_EXCEEDED(HttpStatus.BAD_REQUEST, "프로젝트의 장소 블록 최대 개수를 초과했습니다.(max=%d)", "PB1003");

	private final HttpStatus status;
	private final String message;
	private final String code;
}
