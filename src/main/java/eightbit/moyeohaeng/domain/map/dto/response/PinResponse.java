package eightbit.moyeohaeng.domain.map.dto.response;

import eightbit.moyeohaeng.domain.map.entity.Pin;
import eightbit.moyeohaeng.domain.place.dto.response.PlaceDetail;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "핀 응답 DTO")
public record PinResponse(

	@Schema(description = "핀 ID", example = "1")
	Long id,

	@Schema(description = "프로젝트 ID", example = "10")
	Long projectId,

	@Schema(description = "작성자", example = "test@test.com")
	String author,

	@Schema(description = "장소 상세 정보")
	PlaceDetail place
) {
	public static PinResponse from(Pin pin) {
		return new PinResponse(
			pin.getId(),
			pin.getProject().getId(),
			pin.getAuthor(),
			PlaceDetail.from(pin.getPlace())
		);
	}
}
