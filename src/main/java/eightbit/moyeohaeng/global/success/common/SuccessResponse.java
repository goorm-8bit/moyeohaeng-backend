package eightbit.moyeohaeng.global.success.common;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Getter;

@Getter
public class SuccessResponse<T> {

	private final int status;
	private final String message;

	@JsonInclude(JsonInclude.Include.NON_NULL)
	private final T data;

	private SuccessResponse(SuccessCode successCode, T data) {
		this.status = successCode.getStatus().value();
		this.message = successCode.getMessage();
		this.data = data;
	}

	public static <T> SuccessResponse<T> of(SuccessCode successCode, T data) {
		return new SuccessResponse<>(successCode, data);
	}

	public static SuccessResponse<Void> from(SuccessCode successCode) {
		return new SuccessResponse<>(successCode, null);
	}
}
