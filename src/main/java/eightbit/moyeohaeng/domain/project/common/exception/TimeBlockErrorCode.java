package eightbit.moyeohaeng.domain.project.common.exception;

import org.springframework.http.HttpStatus;

import eightbit.moyeohaeng.global.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TimeBlockErrorCode implements ErrorCode {

	TIME_BLOCK_NOT_FOUND(HttpStatus.NOT_FOUND, "일정을 찾을 수 없습니다.", "TB1001"),
	TIME_BLOCK_CONFLICT(HttpStatus.CONFLICT, "같은 시간대에 다른 일정이 있습니다.", "TB1002");

	private final HttpStatus status;
	private final String message;
	private final String code;
}
