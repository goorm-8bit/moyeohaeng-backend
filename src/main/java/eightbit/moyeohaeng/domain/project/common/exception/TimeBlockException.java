package eightbit.moyeohaeng.domain.project.common.exception;

import eightbit.moyeohaeng.global.exception.BaseException;
import eightbit.moyeohaeng.global.exception.ErrorCode;

public class TimeBlockException extends BaseException {

	public TimeBlockException(ErrorCode errorCode) {
		super(errorCode);
	}

	public TimeBlockException(ErrorCode errorCode, Object... args) {
		super(errorCode, args);
	}
}
