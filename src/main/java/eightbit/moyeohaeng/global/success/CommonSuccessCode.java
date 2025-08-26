package eightbit.moyeohaeng.global.success;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 공통적으로 사용되는 기본 성공 코드 구현체
 */
@Getter
@RequiredArgsConstructor
public enum CommonSuccessCode implements SuccessCode {

	//200
	SELECT_SUCCESS(HttpStatus.OK, "조회 성공"),
	UPDATE_SUCCESS(HttpStatus.OK, "수정 성공"),
	DELETE_SUCCESS(HttpStatus.OK, "삭제 성공"),

	//201 Created
	CREATE_SUCCESS(HttpStatus.CREATED, "생성 성공");

	private final HttpStatus status;
	private final String message;
}
