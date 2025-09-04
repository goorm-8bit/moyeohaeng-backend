package eightbit.moyeohaeng.domain.selection.common.success;

import org.springframework.http.HttpStatus;

import eightbit.moyeohaeng.global.success.SuccessCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PlaceGroupSuccessCode implements SuccessCode {

	CREATE_PLACE_GROUP(HttpStatus.CREATED, "장소 그룹 생성에 성공했습니다."),
	UPDATE_PLACE_BLOCK_TO_GROUPS(HttpStatus.OK, "장소 블록 그룹 정보를 수정했습니다."),
	UPDATE_PLACE_GROUP(HttpStatus.OK, "장소 그룹 수정에 성공했습니다."),
	UPDATE_MEMO(HttpStatus.OK, "메모를 수정했습니다."),
	GET_LIST(HttpStatus.OK, "장소 그룹 목록을 조회했습니다.");

	private final HttpStatus status;
	private final String message;
}
