package eightbit.moyeohaeng.domain.auth.exception;

import eightbit.moyeohaeng.global.exception.common.BaseException;

public class AuthException extends BaseException {
	public AuthException(AuthErrorCode errorCode) {
		super(errorCode);
	}

	public AuthException(AuthErrorCode errorCode, Object... args) {
		super(errorCode, args);
	}
}
