package eightbit.moyeohaeng.global.exception.common;

import org.springframework.http.HttpStatus;

public interface ErrorCode {
	HttpStatus getStatus();

	String getMessage();
	
	String getCode();

	default String getMessage(Object... args) {
		return String.format(this.getMessage(), args);
	}
}
