package eightbit.moyeohaeng.domain.selection.common.success;

import org.springframework.http.HttpStatus;

import eightbit.moyeohaeng.global.success.SuccessCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PlaceBlockCommentSuccessCode implements SuccessCode {

	CREATE_COMMENT(HttpStatus.CREATED, "댓글 생성에 성공했습니다."),
	UPDATE_COMMENT(HttpStatus.OK, "댓글 수정에 성공했습니다."),
	GET_COMMENT_LIST(HttpStatus.OK, "댓글 목록을 조회했습니다."),
	GET_COMMENT_SUMMARY(HttpStatus.OK, "댓글 요약 정보를 조회했습니다.");

	private final HttpStatus status;
	private final String message;
}
