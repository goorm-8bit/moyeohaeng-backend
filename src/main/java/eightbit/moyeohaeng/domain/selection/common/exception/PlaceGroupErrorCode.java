package eightbit.moyeohaeng.domain.selection.common.exception;

import org.springframework.http.HttpStatus;

import eightbit.moyeohaeng.global.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PlaceGroupErrorCode implements ErrorCode {

	PLACE_GROUP_NOT_FOUND(HttpStatus.NOT_FOUND, "장소 그룹을 찾을 수 없습니다.", "PG1001");

	private final HttpStatus status;
	private final String message;
	private final String code;
}
