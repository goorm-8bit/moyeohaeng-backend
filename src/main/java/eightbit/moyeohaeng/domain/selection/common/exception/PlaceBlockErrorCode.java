package eightbit.moyeohaeng.domain.selection.common.exception;

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
	LIMIT_EXCEEDED(HttpStatus.BAD_REQUEST, "프로젝트의 장소 블록 최대 개수를 초과했습니다.(max=%d)", "PB1002"),
	COUNTER_DECREMENT_FAILED(HttpStatus.CONFLICT, "장소 블록 삭제 작업을 처리할 수 없습니다.", "PB1003");

	private final HttpStatus status;
	private final String message;
	private final String code;
}
