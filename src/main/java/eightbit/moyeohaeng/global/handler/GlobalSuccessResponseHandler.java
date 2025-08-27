package eightbit.moyeohaeng.global.handler;

import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import eightbit.moyeohaeng.global.exception.ErrorResponse;
import eightbit.moyeohaeng.global.success.CommonSuccessCode;
import eightbit.moyeohaeng.global.success.SuccessResponse;
import eightbit.moyeohaeng.global.utils.GlobalLogger;
import lombok.RequiredArgsConstructor;

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
@RequiredArgsConstructor
public class GlobalSuccessResponseHandler implements ResponseBodyAdvice<Object> {

	private final ObjectMapper objectMapper;

	@Override
	public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
		return true;
	}

	@Override
	public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
		Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request,
		ServerHttpResponse response) {

		// 실제로 커밋될 HTTP 상태 계산
		HttpStatus resolved = null;
		if (response instanceof ServletServerHttpResponse servletResp) {
			resolved = HttpStatus.resolve(servletResp.getServletResponse().getStatus());
		}
		HttpStatusCode effectiveStatus = (resolved != null) ? resolved : HttpStatus.OK;

		// 204는 본문이 없어야 함 — 정책 선택 필요: 래핑 생략
		if (effectiveStatus.value() == HttpStatus.NO_CONTENT.value()) {
			return null;
		}

		if (body == null) {
			return SuccessResponse.from(mapHttpStatusToSuccessCode(effectiveStatus));
		}

		// 이미 포맷팅된 응답이나 에러는 그대로 반환
		if (body instanceof SuccessResponse || body instanceof ErrorResponse) {
			return body;
		}

		// String은 특별한 처리가 필요함 (JSON 변환 문제 해결)
		if (body instanceof String) {
			try {
				response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
				return objectMapper.writeValueAsString(
					SuccessResponse.of(mapHttpStatusToSuccessCode(effectiveStatus), body)
				);
			} catch (JsonProcessingException e) {
				// JSON 변환 실패 시 로깅하고 에러 응답 반환
				GlobalLogger.error("[응답 직렬화 실패] Failed to serialize String response: " + e.getMessage());
				throw new RuntimeException("응답 직렬화 실패", e);
			}
		}

		// 일반 데이터는 현재 HTTP 상태에 맞는 성공 코드로 처리
		return SuccessResponse.of(mapHttpStatusToSuccessCode(effectiveStatus), body);
	}

	/**
	 * HTTP 상태 코드를 SuccessCode로 매핑
	 */
	private CommonSuccessCode mapHttpStatusToSuccessCode(HttpStatusCode statusCode) {
		if (statusCode.value() == HttpStatus.CREATED.value()) {
			return CommonSuccessCode.CREATE_SUCCESS;
		} else if (statusCode.value() == HttpStatus.OK.value() || statusCode.value() == HttpStatus.ACCEPTED.value()) {
			return CommonSuccessCode.SELECT_SUCCESS;
		} else if (statusCode.value() == HttpStatus.NO_CONTENT.value()) {
			return CommonSuccessCode.DELETE_SUCCESS;
		} else {
			return CommonSuccessCode.SELECT_SUCCESS; // 기본값
		}
	}
}
