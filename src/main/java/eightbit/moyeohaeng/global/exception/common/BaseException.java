package eightbit.moyeohaeng.global.exception.common;

import java.util.Arrays;
import java.util.Objects;

import lombok.Getter;

@Getter
public abstract class BaseException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	private static final Object[] EMPTY_ARGS = {};

	private final ErrorCode errorCode;
	private final transient Object[] args;

	public BaseException(ErrorCode errorCode) {
		super(errorCode.getMessage());
		this.errorCode = Objects.requireNonNull(errorCode, "errorCode must not be null");
		this.args = EMPTY_ARGS;
	}

	public BaseException(ErrorCode errorCode, Object... args) {
		super(errorCode.getMessage());
		this.errorCode = Objects.requireNonNull(errorCode, "errorCode must not be null");
		this.args = (args == null ? EMPTY_ARGS : Arrays.copyOf(args, args.length));
	}

	public String getLogMessage() {
		if (args == null || args.length == 0) {
			return errorCode.getMessage();
		}
		return errorCode.getMessage(args);
	}
}
