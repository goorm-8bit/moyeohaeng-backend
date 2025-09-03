package eightbit.moyeohaeng.domain.project.common.exception;

import eightbit.moyeohaeng.global.exception.BaseException;
import eightbit.moyeohaeng.global.exception.ErrorCode;

public class PlaceException extends BaseException {

	public PlaceException(ErrorCode errorCode) {
		super(errorCode);
	}

	public PlaceException(ErrorCode errorCode, Object... args) {
		super(errorCode, args);
	}
}
