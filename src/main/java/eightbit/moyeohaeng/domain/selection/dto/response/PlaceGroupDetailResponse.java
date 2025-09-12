package eightbit.moyeohaeng.domain.selection.dto.response;

import java.util.List;

import eightbit.moyeohaeng.domain.selection.entity.PlaceGroup;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "장소 그룹 조회 응답 DTO")
public record PlaceGroupDetailResponse(
	@Schema(description = "장소 그룹 ID", example = "1")
	Long id,

	@Schema(description = "그룹 이름", example = "점심 먹을 곳")
	String name,

	@Schema(description = "장소 블록")
	List<PlaceBlockSearchResponse> placeBlocks
) {
	public static PlaceGroupDetailResponse empty(PlaceGroup placeGroup) {
		return new PlaceGroupDetailResponse(placeGroup.getId(), placeGroup.getName(), List.of());
	}

	public static PlaceGroupDetailResponse of(PlaceGroup placeGroup, List<PlaceBlockSearchResponse> placeBlocks) {
		return new PlaceGroupDetailResponse(placeGroup.getId(), placeGroup.getName(), placeBlocks);
	}
}
