package eightbit.moyeohaeng.domain.map.common.success;

import org.springframework.http.HttpStatus;

import eightbit.moyeohaeng.global.success.SuccessCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PinSuccessCode implements SuccessCode {

	CREATE_PIN(HttpStatus.CREATED, "핀 생성에 성공했습니다."),
	GET_PIN_LIST(HttpStatus.OK, "핀 목록을 조회했습니다."),
	DELETE_PIN(HttpStatus.NO_CONTENT, "핀을 삭제했습니다.");

	private final HttpStatus status;
	private final String message;
}
