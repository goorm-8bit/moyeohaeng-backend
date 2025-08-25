package eightbit.moyeohaeng.global.success.common;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

class BaseSuccessResponseTest {

	@Test
	@DisplayName("of() 메서드 - 데이터와 함께 SuccessResponse 객체를 생성")
	void testOfMethod_withData() {
		// given
		SuccessCode successCode = CommonSuccessCode.SELECT_SUCCESS;
		String testData = "test data";

		// when
		SuccessResponse<String> response = SuccessResponse.of(successCode, testData);

		// then
		assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
		assertThat(response.getMessage()).isEqualTo(successCode.getMessage());
		assertThat(response.getData()).isEqualTo(testData);
	}

	@Test
	@DisplayName("from() 메서드 - 데이터 없이 SuccessResponse 객체를 생성")
	void testFromMethod_withoutData() {
		// given
		SuccessCode successCode = CommonSuccessCode.CREATE_SUCCESS;

		// when
		SuccessResponse<Void> response = SuccessResponse.from(successCode);

		// then
		assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
		assertThat(response.getMessage()).isEqualTo(successCode.getMessage());
		assertThat(response.getData()).isNull();
	}

	@Test
	@DisplayName("다양한 SuccessCode 구현체와 함께 SuccessResponse 사용")
	void test_withDifferentSuccessCode_implementations() {
		// given
		SuccessCode commonCode = CommonSuccessCode.UPDATE_SUCCESS;
		SuccessCode customCode = new CustomSuccessCode();

		// when
		SuccessResponse<String> commonResponse = SuccessResponse.of(commonCode, "common data");
		SuccessResponse<String> customResponse = SuccessResponse.of(customCode, "custom data");

		// then
		assertThat(commonResponse.getStatus()).isEqualTo(HttpStatus.OK.value());
		assertThat(commonResponse.getMessage()).isEqualTo(commonCode.getMessage());

		assertThat(customResponse.getStatus()).isEqualTo(HttpStatus.ACCEPTED.value());
		assertThat(customResponse.getMessage()).isEqualTo("Custom success message");
	}

	@Test
	@DisplayName("JSON 직렬화를 위한 getter 메서드 확인")
	void testGetterMethods() {
		// given
		SuccessCode successCode = CommonSuccessCode.SELECT_SUCCESS;
		String testData = "test data";

		// when
		SuccessResponse<String> response = SuccessResponse.of(successCode, testData);

		// then
		assertThat(response.getStatus()).isNotNull();
		assertThat(response.getMessage()).isNotNull();
		assertThat(response.getData()).isNotNull();
	}

	// 테스트용 커스텀 SuccessCode 구현체
	private static class CustomSuccessCode implements SuccessCode {
		@Override
		public HttpStatus getStatus() {
			return HttpStatus.ACCEPTED;
		}

		@Override
		public String getMessage() {
			return "Custom success message";
		}

		@Override
		public String name() {
			return "CUSTOM_SUCCESS";
		}
	}
}
