package eightbit.moyeohaeng.domain.project.dto.response;

import eightbit.moyeohaeng.domain.project.entity.PlaceBlock;
import lombok.Builder;

/**
 * PlaceBlock 단건 응답 DTO.
 * Entity를 직렬화하여 API 응답으로 변환한다.
 */
@Builder
public record PlaceBlockResponse(
	Long id,
	Long projectId,
	String name,
	String address,
	Double latitude,
	Double longitude,
	String memo,
	String detailLink,
	String category,
	String color,
	String author
) {
	public static PlaceBlockResponse from(PlaceBlock e) {
		return PlaceBlockResponse.builder()
			.id(e.getId())
			.projectId(e.getProjectId())
			.name(e.getName())
			.address(e.getAddress())
			.latitude(e.getLatitude())
			.longitude(e.getLongitude())
			.memo(e.getMemo())
			.detailLink(e.getDetailLink())
			.category(e.getCategory())
			.color(e.getColor())
			.author(e.getAuthor())
			.build();
	}
}
