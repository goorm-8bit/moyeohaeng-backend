package eightbit.moyeohaeng.domain.project.dto.response;

import java.time.LocalDate;
import java.time.LocalTime;

import eightbit.moyeohaeng.domain.project.entity.PlaceBlock;
import eightbit.moyeohaeng.domain.project.entity.PlaceBlockType;
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
	LocalDate date,
	LocalTime time,
	String reviewLink,
	String detailLink,
	String category,
	String color,
	String author,
	PlaceBlockType type
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
			.date(e.getDate())
			.time(e.getTime())
			.reviewLink(e.getReviewLink())
			.detailLink(e.getDetailLink())
			.category(e.getCategory())
			.color(e.getColor())
			.author(e.getAuthor())
			.type(e.getType())
			.build();
	}
}
