package eightbit.moyeohaeng.domain.selection.common.exception;

import org.springframework.http.HttpStatus;

import eightbit.moyeohaeng.global.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PlaceBlockCommentErrorCode implements ErrorCode {

	COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "댓글을 찾을 수 없습니다.", "PBC1001"),
	FORBIDDEN(HttpStatus.FORBIDDEN, "댓글에 대한 권한이 없습니다.", "PBC1002");

	private final HttpStatus status;
	private final String message;
	private final String code;
}
