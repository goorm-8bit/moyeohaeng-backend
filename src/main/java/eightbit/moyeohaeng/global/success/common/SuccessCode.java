package eightbit.moyeohaeng.global.success.common;

import org.springframework.http.HttpStatus;

/**
 * API 응답의 성공 코드를 정의하는 인터페이스
 * 도메인별로 구현체를 생성하여 사용할 수 있습니다.
 */
public interface SuccessCode {

	HttpStatus getStatus();

	String getMessage();

	String name();
}
