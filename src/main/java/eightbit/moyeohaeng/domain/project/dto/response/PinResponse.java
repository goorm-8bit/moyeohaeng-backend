package eightbit.moyeohaeng.domain.project.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "핀 응답 DTO")
public record PinResponse(

	@Schema(description = "핀 ID", example = "1")
	Long id,

	@Schema(description = "주소", example = "부산광역시 해운대구 우동")
	String address,

	@Schema(description = "위도", example = "35.1587")
	Double latitude,

	@Schema(description = "경도", example = "129.1604")
	Double longitude,

	@Schema(description = "상세 링크", example = "https://example.com/pin/1")
	String detailLink,

	@Schema(description = "카테고리", example = "맛집")
	String category,

	@Schema(description = "작성자", example = "test@test.com")
	String author
) {
	/*
	public static PinResponse from(Pin pin) {
		return new PinResponse(
			pin.getId(),
			pin.getAddress(),
			pin.getLatitude(),
			pin.getLongitude(),
			pin.getDetailLink(),
			pin.getCategory(),
			pin.getAuthor()
		);
	}
	 */
}
