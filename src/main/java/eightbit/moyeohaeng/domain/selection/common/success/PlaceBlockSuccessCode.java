package eightbit.moyeohaeng.domain.selection.common.success;

import org.springframework.http.HttpStatus;

import eightbit.moyeohaeng.global.success.SuccessCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PlaceBlockSuccessCode implements SuccessCode {

	CREATE_PLACE_BLOCK(HttpStatus.CREATED, "장소 블록 생성에 성공했습니다."),
	UPDATE_MEMO(HttpStatus.OK, "메모를 수정했습니다."),
	GET_LIST(HttpStatus.OK, "장소 블록 목록을 조회했습니다.");

	private final HttpStatus status;
	private final String message;
}
