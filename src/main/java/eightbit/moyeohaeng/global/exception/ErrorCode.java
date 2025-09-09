package eightbit.moyeohaeng.global.exception;

import org.springframework.http.HttpStatus;

public interface ErrorCode {
	HttpStatus getStatus();

	String getMessage();
	
	String getCode();

	default String getMessage(Object... args) {
		return String.format(this.getMessage(), args);
	}
}
