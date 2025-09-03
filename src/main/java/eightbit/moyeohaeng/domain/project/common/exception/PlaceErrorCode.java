package eightbit.moyeohaeng.domain.project.common.exception;

import org.springframework.http.HttpStatus;

import eightbit.moyeohaeng.global.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PlaceErrorCode implements ErrorCode {

	PLACE_NOT_FOUND(HttpStatus.NOT_FOUND, "장소를 찾을 수 없습니다.", "P1001");

	private final HttpStatus status;
	private final String message;
	private final String code;
}
