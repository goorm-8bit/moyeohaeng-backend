package eightbit.moyeohaeng.domain.member.exception;

import eightbit.moyeohaeng.global.exception.common.BaseException;

public class MemberException extends BaseException {

	public MemberException(MemberErrorCode errorCode) {
		super(errorCode);
	}

}
