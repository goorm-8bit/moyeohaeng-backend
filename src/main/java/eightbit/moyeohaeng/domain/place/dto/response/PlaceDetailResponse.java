package eightbit.moyeohaeng.domain.place.dto.response;

import eightbit.moyeohaeng.domain.place.entity.Place;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "장소 상세 응답 DTO")
public record PlaceDetailResponse(

	@Schema(description = "장소 ID", example = "1")
	Long id,

	@Schema(description = "장소명", example = "광안리해수욕장")
	String name,

	@Schema(description = "주소", example = "부산광역시 수영구 광안해변로 219")
	String address,

	@Schema(description = "위도", example = "35.1531")
	Double latitude,

	@Schema(description = "경도", example = "129.1187")
	Double longitude,

	@Schema(description = "상세 링크", example = "https://example.com/places/1")
	String detailLink,

	@Schema(description = "카테고리", example = "해변")
	String category
) {
	public static PlaceDetailResponse from(Place place) {
		return new PlaceDetailResponse(
			place.getId(),
			place.getName(),
			place.getAddress(),
			place.getLatitude(),
			place.getLongitude(),
			place.getDetailLink(),
			place.getCategory()
		);
	}
}

