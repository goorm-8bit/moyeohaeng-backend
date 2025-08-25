package eightbit.moyeohaeng.domain.project.plan.dto.response;

import java.util.List;

import org.springframework.data.domain.Page;

import eightbit.moyeohaeng.domain.project.plan.entity.PlaceBlock;

/**
 * PlaceBlock 페이지 응답 DTO.
 * 페이징된 PlaceBlockResponse 목록과 메타데이터를 포함한다.
 */
public record PlaceBlockPageResponse(
	List<PlaceBlockResponse> content,
	int page,
	int size,
	long totalElements
) {
	public static PlaceBlockPageResponse from(Page<PlaceBlock> page) {
		List<PlaceBlockResponse> content = page.getContent().stream()
			.map(PlaceBlockResponse::from)
			.toList();

		return new PlaceBlockPageResponse(
			content,
			page.getNumber(),
			page.getSize(),
			page.getTotalElements()
		);
	}
}