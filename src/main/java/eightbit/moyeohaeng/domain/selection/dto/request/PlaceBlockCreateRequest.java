package eightbit.moyeohaeng.domain.selection.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record PlaceBlockCreateRequest(
	@Schema(description = "장소 ID", example = "1")
	@NotNull(message = "장소 ID는 필수 입력값입니다.")
	Long placeId
) {
}
