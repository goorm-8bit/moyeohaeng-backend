package eightbit.moyeohaeng.domain.team.exception;

import org.springframework.http.HttpStatus;

import eightbit.moyeohaeng.global.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TeamErrorCode implements ErrorCode {

	//
	TEAM_NOT_FOUND(HttpStatus.NOT_FOUND, "팀을 찾을 수 없습니다.", "T1000"),
	NOT_HAVE_RIGHT(HttpStatus.FORBIDDEN, "권한이 없습니다.", "T1001"),
	YOU_NOT_TEAM_MEMBER(HttpStatus.FORBIDDEN, "팀 소속 멤버가 아닙니다.", "T1002");

	private final HttpStatus status;
	private final String message;
	private final String code;
}
