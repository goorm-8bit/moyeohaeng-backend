package eightbit.moyeohaeng.domain.project.plan.exception;

import eightbit.moyeohaeng.global.exception.common.BaseException;

/**
 * PlaceBlock 도메인 전용 예외.
 * ErrorCode를 받아 BaseException으로 위임한다.
 */
public class PlaceBlockException extends BaseException {
	private static final long serialVersionUID = 1L;

	public PlaceBlockException(PlaceBlockErrorCode errorCode, Object... args) {
		super(errorCode, args);
	}
}
