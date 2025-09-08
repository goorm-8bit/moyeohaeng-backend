package eightbit.moyeohaeng.domain.team.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class MemberNotHaveRightException extends RuntimeException {
	
	public MemberNotHaveRightException(String message) {
		super(message);
	}
}
