package eightbit.moyeohaeng.domain.member.common.exception;

import eightbit.moyeohaeng.global.exception.BaseException;

public class MemberException extends BaseException {

	public MemberException(MemberErrorCode errorCode) {
		super(errorCode);
	}

}
