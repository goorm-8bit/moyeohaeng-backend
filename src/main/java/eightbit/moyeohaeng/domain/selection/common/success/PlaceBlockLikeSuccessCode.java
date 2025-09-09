package eightbit.moyeohaeng.domain.selection.common.success;

import org.springframework.http.HttpStatus;

import eightbit.moyeohaeng.global.success.SuccessCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PlaceBlockLikeSuccessCode implements SuccessCode {

	TOGGLE_LIKE(HttpStatus.OK, "장소 블록 좋아요 상태를 변경했습니다."),
	GET_LIKE_SUMMARY(HttpStatus.OK, "장소 블록 좋아요 요약 정보를 조회했습니다.");

	private final HttpStatus status;
	private final String message;
}
