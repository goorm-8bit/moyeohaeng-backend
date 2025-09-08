package eightbit.moyeohaeng.domain.itinerary.common.success;

import org.springframework.http.HttpStatus;

import eightbit.moyeohaeng.global.success.SuccessCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TimeBlockSuccessCode implements SuccessCode {

	CREATE_TIME_BLOCK(HttpStatus.CREATED, "시간 블록 생성에 성공했습니다."),
	UPDATE_TIME_BLOCK(HttpStatus.OK, "시간 블록 수정에 성공했습니다.");

	private final HttpStatus status;
	private final String message;
}
