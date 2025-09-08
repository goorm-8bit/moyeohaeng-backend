package eightbit.moyeohaeng.domain.map.common.exception;

import eightbit.moyeohaeng.global.exception.BaseException;
import eightbit.moyeohaeng.global.exception.ErrorCode;

public class PinException extends BaseException {
	public PinException(ErrorCode errorCode) {
		super(errorCode);
	}

	public PinException(ErrorCode errorCode, Object... args) {
		super(errorCode, args);
	}
}
