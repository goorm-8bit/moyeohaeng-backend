package eightbit.moyeohaeng.global.exception.handler;

import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import org.springframework.http.converter.HttpMessageNotReadableException;

import java.util.stream.Collectors;

import eightbit.moyeohaeng.global.exception.common.BaseException;
import eightbit.moyeohaeng.global.exception.common.ErrorCode;
import eightbit.moyeohaeng.global.exception.common.ErrorResponse;
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
