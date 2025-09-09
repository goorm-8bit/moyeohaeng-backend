package eightbit.moyeohaeng.global.dto;

import java.util.List;
import java.util.function.Function;

import org.springframework.data.domain.Page;

/**
 * 페이징 응답 DTO.
 * 어떤 타입이든 페이징된 목록과 메타데이터를 포함한다.
 *
 * @param <T> 응답 목록의 타입
 */
public record PageResponse<T>(
	List<T> content,
	int page,
	int size,
	long totalElements
) {

	public static <T, U> PageResponse<T> from(Page<U> page, Function<U, T> converter) {
		List<T> content = page.getContent().stream()
			.map(converter)
			.toList();

		return new PageResponse<>(
			content,
			page.getNumber(),
			page.getSize(),
			page.getTotalElements()
		);
	}
}