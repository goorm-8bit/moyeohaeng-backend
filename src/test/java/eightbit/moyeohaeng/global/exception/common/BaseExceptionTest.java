package eightbit.moyeohaeng.global.exception.common;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import eightbit.moyeohaeng.global.exception.BaseException;
import eightbit.moyeohaeng.global.exception.ErrorCode;

class BaseExceptionTest {

	// 테스트용 ErrorCode 구현
	enum TestErrorCode implements ErrorCode {
		TEST_CODE("테스트 메시지"),
		FORMAT_CODE("포맷 메시지: %s, %d");

		private final String message;

		TestErrorCode(String message) {
			this.message = message;
		}

		@Override
		public HttpStatus getStatus() {
			return HttpStatus.INTERNAL_SERVER_ERROR;
		}

		@Override
		public String getMessage() {
			return message;
		}

		@Override
		public String getCode() {
			return name();
		}
	}

	@DisplayName("인자 없는 BaseException 생성 시, getMessage()가 ErrorCode의 기본 메시지를 반환한다.")
	@Test
	void getMessage_withoutArgs_shouldReturnDefaultMessage() {
		// given
		ErrorCode code = TestErrorCode.TEST_CODE;
		BaseException exception = new BaseException(code) {
		};

		// when
		String message = exception.getMessage();

		// then
		assertThat(message).isEqualTo(code.getMessage());
	}

	@DisplayName("인자 있는 BaseException 생성 시, getMessage()가 포맷팅된 메시지를 반환한다.")
	@Test
	void getMessage_withArgs_shouldReturnFormattedMessage() {
		// given
		ErrorCode code = TestErrorCode.FORMAT_CODE;
		Object[] args = {"A", 1};
		BaseException exception = new BaseException(code, args) {
		};

		// when
		String message = exception.getMessage();

		// then
		assertThat(message).isEqualTo("포맷 메시지: A, 1");
	}

	@DisplayName("getArgs()는 방어적 복사본을 반환하여 내부 배열의 수정을 방지한다.")
	@Test
	void getArgs_shouldReturnDefensiveCopy() {
		// given
		Object[] originalArgs = {"A", 1};
		BaseException exception = new BaseException(TestErrorCode.FORMAT_CODE, originalArgs) {
		};

		// when
		Object[] retrievedArgs = exception.getArgs();
		retrievedArgs[0] = "B"; // 복사본 수정

		// then
		assertThat(exception.getArgs()[0]).isEqualTo("A");
		assertThat(exception.getArgs()).isNotSameAs(originalArgs);
	}
}
