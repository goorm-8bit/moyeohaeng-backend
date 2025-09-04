package eightbit.moyeohaeng.domain.map.dto.response;

import eightbit.moyeohaeng.domain.map.entity.Pin;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "핀 응답 DTO")
public record PinResponse(

	@Schema(description = "핀 ID", example = "1")
	Long id,

	@Schema(description = "프로젝트 ID", example = "10")
	Long projectId,

	@Schema(description = "장소 ID", example = "5")
	Long placeId,

	@Schema(description = "기록자", example = "test@test.com")
	String author
) {
	public static PinResponse from(Pin pin) {
		return new PinResponse(
			pin.getId(),
			pin.getProject().getId(),
			pin.getPlace().getId(),
			pin.getAuthor()
		);
	}
}
