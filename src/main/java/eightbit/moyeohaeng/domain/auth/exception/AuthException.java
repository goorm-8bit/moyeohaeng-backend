package eightbit.moyeohaeng.domain.auth.exception;

import eightbit.moyeohaeng.global.exception.common.BaseException;
import eightbit.moyeohaeng.global.exception.common.ErrorCode;

public class AuthException extends BaseException {
    public AuthException(ErrorCode errorCode) {
        super(errorCode);
    }
}
