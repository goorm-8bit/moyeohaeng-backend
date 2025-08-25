package eightbit.moyeohaeng.global.success.handler;

import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import eightbit.moyeohaeng.global.exception.common.ErrorResponse;
import eightbit.moyeohaeng.global.success.common.CommonSuccessCode;
import eightbit.moyeohaeng.global.success.common.SuccessCode;
import eightbit.moyeohaeng.global.success.common.SuccessResponse;

/**
 * API 응답을 표준화하기 위한 글로벌 어드바이스입니다.
 * 모든 컨트롤러의 응답을 자동으로 SuccessResponse 형태로 변환합니다.
 *
 * 응답 처리 규칙:
 * 1. 이미 SuccessResponse나 ErrorResponse로 감싸진 응답은 그대로 반환
 * 2. ResponseEntity를 반환하는 경우 내부 객체를 SuccessResponse로 변환하고 상태 코드는 유지
 * 3. 그 외의 일반 데이터는 자동으로 SuccessCode.SELECT_SUCCESS로 래핑
 *
 * 사용 예시:
 * - 명시적 응답: return SuccessResponse.of(SuccessCode.CREATE_SUCCESS, newEntity);
 * - 자동 변환 응답: return userList;  // SELECT_SUCCESS로 자동 변환
 * - HTTP 상태 지정: return ResponseEntity.status(HttpStatus.CREATED).body(newEntity);
 */
@RestControllerAdvice(basePackages = "eightbit.moyeohaeng")
public class GlobalSuccessResponseHandler implements ResponseBodyAdvice<Object> {

	@Override
	public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
		return true;
	}

	@Override
	public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
		Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request,
		ServerHttpResponse response) {

		if (body == null) {
			return SuccessResponse.from(CommonSuccessCode.SELECT_SUCCESS);
		}

		// 이미 포맷팅된 응답이나 에러는 그대로 반환
		if (body instanceof SuccessResponse || body instanceof ErrorResponse) {
			return body;
		}

		// ResponseEntity 처리 
		if (body instanceof ResponseEntity<?> responseEntity) {
			Object responseBody = responseEntity.getBody();

			if (responseBody == null) {
				SuccessCode successCode = mapHttpStatusToSuccessCode(responseEntity.getStatusCode());
				return ResponseEntity
					.status(responseEntity.getStatusCode())
					.headers(responseEntity.getHeaders())
					.body(SuccessResponse.from(successCode));
			}

			// 이미 래핑된 객체는 그대로 유지
			if (responseBody instanceof SuccessResponse || responseBody instanceof ErrorResponse) {
				return responseEntity;
			}

			// 상태코드에 맞는 SuccessCode 적용
			SuccessCode successCode = mapHttpStatusToSuccessCode(responseEntity.getStatusCode());
			return ResponseEntity
				.status(responseEntity.getStatusCode())
				.headers(responseEntity.getHeaders())
				.body(SuccessResponse.of(successCode, responseBody));
		}

		// String은 특별한 처리가 필요함 (JSON 변환 문제 해결)
		if (body instanceof String) {
			try {
				ObjectMapper objectMapper = new ObjectMapper();
				return objectMapper.writeValueAsString(
					SuccessResponse.of(CommonSuccessCode.SELECT_SUCCESS, body)
				);
			} catch (JsonProcessingException e) {
				return body;
			}
		}

		// 일반 데이터는 조회 성공으로 처리
		return SuccessResponse.of(CommonSuccessCode.SELECT_SUCCESS, body);
	}

	/**
	 * HTTP 상태 코드를 SuccessCode로 매핑
	 */
	private SuccessCode mapHttpStatusToSuccessCode(HttpStatusCode statusCode) {
		if (statusCode == HttpStatus.CREATED) {
			return CommonSuccessCode.CREATE_SUCCESS;
		} else if (statusCode == HttpStatus.OK || statusCode == HttpStatus.ACCEPTED) {
			return CommonSuccessCode.SELECT_SUCCESS;
		} else if (statusCode == HttpStatus.NO_CONTENT) {
			return CommonSuccessCode.DELETE_SUCCESS;
		} else {
			return CommonSuccessCode.SELECT_SUCCESS; // 기본값
		}
	}
}
