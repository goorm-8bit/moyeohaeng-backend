package eightbit.moyeohaeng.domain.selection.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "장소 블록 삭제 응답 DTO")
public record PlaceBlockDeleteResponse(
	@Schema(description = "장소 블록 ID", example = "1")
	Long id
) {
	public static PlaceBlockDeleteResponse of(Long id) {
		return new PlaceBlockDeleteResponse(id);
	}
}
