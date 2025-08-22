package eightbit.moyeohaeng.domain.member.exception;

import org.springframework.http.HttpStatus;

import eightbit.moyeohaeng.global.exception.common.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MemberErrorCode implements ErrorCode {

	MEMBER_NOT_FOUND("사용자를 찾을 수 없습니다.", HttpStatus.NOT_FOUND, "M1001");

	private final String message;
	private final HttpStatus status;
	private final String code;
}
