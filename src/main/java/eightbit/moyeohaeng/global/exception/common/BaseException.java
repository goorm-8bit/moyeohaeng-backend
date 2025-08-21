package eightbit.moyeohaeng.global.exception.common;

import lombok.Getter;

@Getter
public abstract class BaseException extends RuntimeException {

	private final ErrorCode errorCode;
	private final transient Object[] args;

	public BaseException(ErrorCode errorCode) {
		super(errorCode.getMessage());
		this.errorCode = errorCode;
		this.args = new Object[]{};
	}

	public BaseException(ErrorCode errorCode, Object... args) {
		super(errorCode.getMessage());
		this.errorCode = errorCode;
		this.args = args;
	}

	public String getLogMessage() {
		if (args == null || args.length == 0) {
			return errorCode.getMessage();
		}
		return errorCode.getMessage(args);
	}
}
