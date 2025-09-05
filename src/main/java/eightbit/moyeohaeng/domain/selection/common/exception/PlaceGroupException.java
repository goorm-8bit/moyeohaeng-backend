package eightbit.moyeohaeng.domain.selection.common.exception;

import eightbit.moyeohaeng.global.exception.BaseException;
import eightbit.moyeohaeng.global.exception.ErrorCode;

public class PlaceGroupException extends BaseException {

	public PlaceGroupException(ErrorCode errorCode) {
		super(errorCode);
	}

	public PlaceGroupException(ErrorCode errorCode, Object... args) {
		super(errorCode, args);
	}
}
