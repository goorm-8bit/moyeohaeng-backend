package eightbit.moyeohaeng.domain.selection.dto.response;

import java.time.LocalDateTime;

import eightbit.moyeohaeng.domain.place.entity.Place;
import eightbit.moyeohaeng.domain.selection.entity.PlaceBlock;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "장소 블록 생성 요청 DTO")
public record PlaceBlockCreateResponse(
	@Schema(description = "장소 블록 ID", example = "1")
	Long id,

	@Schema(description = "장소 이름", example = "성산일출봉")
	String name,

	@Schema(description = "주소", example = "제주특별자치도 서귀포시 성산읍 일출로 284-12")
	String address,

	@Schema(description = "위도", example = "33.4589")
	Double latitude,

	@Schema(description = "경도", example = "126.9421")
	Double longitude,

	@Schema(description = "상세 링크(선택)", example = "https://map.naver.com/v5/entry/place/384169")
	String detailLink,

	@Schema(description = "카테고리", example = "관광지")
	String category,

	@Schema(description = "메모")
	String memo,

	@Schema(description = "생성 시각", example = "2025-09-05T10:50:03.3834426")
	LocalDateTime createdAt
) {
	public static PlaceBlockCreateResponse of(PlaceBlock placeBlock, Place place) {
		return new PlaceBlockCreateResponse(
			placeBlock.getId(),
			place.getName(),
			place.getAddress(),
			place.getLatitude(),
			place.getLongitude(),
			place.getDetailLink(),
			place.getCategory(),
			placeBlock.getMemo(),
			placeBlock.getCreatedAt()
		);
	}
}
