package eightbit.moyeohaeng.global.handler;

import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.async.AsyncRequestNotUsableException;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import eightbit.moyeohaeng.global.exception.BaseException;
import eightbit.moyeohaeng.global.exception.ErrorCode;
import eightbit.moyeohaeng.global.exception.ErrorResponse;
import eightbit.moyeohaeng.global.exception.GlobalErrorCode;
import eightbit.moyeohaeng.global.utils.GlobalLogger;

/**
 * 전역 예외 처리를 위한 베이스 예외.
 * <p>메시지는 {@link ErrorCode}와 선택적 포맷 인자(args)를 기반으로 생성됩니다.</p>
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(BaseException.class)
	public ResponseEntity<ErrorResponse> handleBaseException(BaseException e) {
		return getErrorResponse(e, e.getErrorCode(), e.getArgs());
	}

	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException e) {
		GlobalLogger.error(e.toString());

		return ResponseEntity
			.status(HttpStatus.BAD_REQUEST)
			.body(ErrorResponse.of(GlobalErrorCode.INVALID_INPUT, e.getMessage()));
	}

	@ExceptionHandler(RuntimeException.class)
	public ResponseEntity<ErrorResponse> handleRuntimeException(RuntimeException e) {
		return getErrorResponse(e, GlobalErrorCode.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(ResponseStatusException.class)
	public ResponseEntity<ErrorResponse> handleResponseStatusException(ResponseStatusException e) {
		GlobalLogger.error(e.toString());
		HttpStatus status = HttpStatus.valueOf(e.getStatusCode().value());

		GlobalErrorCode mapped = switch (status) {
			case FORBIDDEN -> GlobalErrorCode.FORBIDDEN;
			case NOT_FOUND -> GlobalErrorCode.RESOURCE_NOT_FOUND;
			case BAD_REQUEST -> GlobalErrorCode.INVALID_INPUT;
			case METHOD_NOT_ALLOWED -> GlobalErrorCode.METHOD_NOT_ALLOWED;
			default -> GlobalErrorCode.INTERNAL_SERVER_ERROR;
		};

		String reason = e.getReason();
		ErrorResponse body = (reason == null || reason.isBlank())
			? ErrorResponse.of(mapped)
			: ErrorResponse.of(mapped, reason);

		return ResponseEntity.status(status).body(body);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> handleException(Exception e) {
		return getErrorResponse(e, GlobalErrorCode.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(MissingRequestHeaderException.class)
	public ResponseEntity<ErrorResponse> handleMissingRequestHeaderException(MissingRequestHeaderException e) {
		return getErrorResponse(e, GlobalErrorCode.MISSING_HEADER);
	}

	@ExceptionHandler(NoResourceFoundException.class)
	public ResponseEntity<ErrorResponse> handleNoResourceException(NoResourceFoundException e) {
		return getErrorResponse(e, GlobalErrorCode.RESOURCE_NOT_FOUND);
	}

	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	public ResponseEntity<ErrorResponse>
	handleMethodNotAllowedException(HttpRequestMethodNotSupportedException e) {
		return getErrorResponse(e, GlobalErrorCode.METHOD_NOT_ALLOWED);
	}

	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
		GlobalLogger.error(e.toString());

		return ResponseEntity
			.status(HttpStatus.BAD_REQUEST)
			.body(ErrorResponse.of(GlobalErrorCode.INVALID_INPUT));
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
		GlobalLogger.error(e.toString());

		String errorMessage = e.getBindingResult().getAllErrors().stream()
			.map(error -> error.getDefaultMessage())
			.collect(Collectors.joining(", "));

		return ResponseEntity
			.status(HttpStatus.BAD_REQUEST)
			.body(ErrorResponse.of(GlobalErrorCode.INVALID_INPUT, errorMessage));
	}

	@ExceptionHandler(AsyncRequestNotUsableException.class)
	public void handleAsyncRequestNotUsableException() {
		// SSE 같은 비동기 응답에서, 이미 응답이 끝났거나 오류로 인해 더 이상 응답 스트림에 쓰기 불가능할 때 발생하는 예외
		// https://github.com/spring-projects/spring-framework/issues/32509
	}

	private static ResponseEntity<ErrorResponse> getErrorResponse(Exception e, GlobalErrorCode errorCode) {
		GlobalLogger.error(e.toString());

		return ResponseEntity
			.status(errorCode.getStatus())
			.body(ErrorResponse.of(errorCode));
	}

	private static ResponseEntity<ErrorResponse> getErrorResponse(Exception e, ErrorCode errorCode, Object... args) {
		GlobalLogger.error(e.toString());

		return ResponseEntity
			.status(errorCode.getStatus())
			.body(ErrorResponse.of(errorCode, errorCode.getMessage(args)));
	}

}
