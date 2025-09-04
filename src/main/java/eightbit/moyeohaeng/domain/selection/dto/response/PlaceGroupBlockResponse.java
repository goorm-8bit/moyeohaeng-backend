package eightbit.moyeohaeng.domain.selection.dto.response;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "장소 그룹 블록 응답 DTO")
public record PlaceGroupBlockResponse(
	@Schema(description = "장소 블록 ID", example = "1")
	Long placeBlockId,

	@Schema(description = "장소 그룹 ID", example = "[1, 2, 3]")
	List<Long> placeGroupIds
) {
	public static PlaceGroupBlockResponse of(Long placeBlockId, List<Long> placeGroupIds) {
		return new PlaceGroupBlockResponse(placeBlockId, placeGroupIds);
	}
}
