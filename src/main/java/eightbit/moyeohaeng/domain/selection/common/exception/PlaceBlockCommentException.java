package eightbit.moyeohaeng.domain.selection.common.exception;

import eightbit.moyeohaeng.global.exception.BaseException;

public class PlaceBlockCommentException extends BaseException {

	public PlaceBlockCommentException(PlaceBlockCommentErrorCode errorCode) {
		super(errorCode);
	}
}
