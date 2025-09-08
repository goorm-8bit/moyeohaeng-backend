package eightbit.moyeohaeng.domain.team.exception;

import eightbit.moyeohaeng.global.exception.BaseException;

public class TeamException extends BaseException {

	/**
	 * 지정한 에러코드로 예외를 생성합니다.
	 *
	 * @param errorCode Team 도메인 에러코드(필수)
	 */
	public TeamException(TeamErrorCode errorCode) {
		super(errorCode);
	}

	/**
	 * 지정한 에러코드와 메시지 포맷 인자를 받아 예외를 생성합니다.
	 *
	 * @param errorCode Team 도메인 에러코드(필수)
	 * @param args      에러 메시지 템플릿에 바인딩할 인자(선택)
	 */
	public TeamException(TeamErrorCode errorCode, Object... args) {
		super(errorCode, args);
	}
}
