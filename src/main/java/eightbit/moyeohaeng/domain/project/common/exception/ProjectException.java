package eightbit.moyeohaeng.domain.project.common.exception;

import eightbit.moyeohaeng.global.exception.BaseException;

/**
 * Project 도메인 전용 예외.
 * ErrorCode를 받아 BaseException으로 위임한다.
 */
public class ProjectException extends BaseException {
	/**
	 * 지정한 에러코드로 예외를 생성합니다.
	 *
	 * @param errorCode Project 도메인 에러코드(필수)
	 */
	public ProjectException(ProjectErrorCode errorCode) {
		super(errorCode);
	}

	/**
	 * 지정한 에러코드와 메시지 포맷 인자를 받아 예외를 생성합니다.
	 *
	 * @param errorCode Project 도메인 에러코드(필수)
	 * @param args      에러 메시지 템플릿에 바인딩할 인자(선택)
	 */
	public ProjectException(ProjectErrorCode errorCode, Object... args) {
		super(errorCode, args);
	}
}
