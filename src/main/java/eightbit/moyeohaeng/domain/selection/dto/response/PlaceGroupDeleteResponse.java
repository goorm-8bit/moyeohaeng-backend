package eightbit.moyeohaeng.domain.selection.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "장소 그룹 삭제 응답 DTO")
public record PlaceGroupDeleteResponse(
	@Schema(description = "장소 그룹 ID", example = "1")
	Long id
) {
	public static PlaceGroupDeleteResponse of(Long id) {
		return new PlaceGroupDeleteResponse(id);
	}
}
