package eightbit.moyeohaeng.domain.selection.dto.response;

import java.util.List;

import eightbit.moyeohaeng.domain.selection.entity.PlaceGroup;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "장소 그룹 응답 DTO")
public record PlaceGroupResponse(
	@Schema(description = "장소 그룹 ID", example = "1")
	Long id,

	@Schema(description = "그룹 이름", example = "점심 먹을 곳")
	String name,

	@Schema(description = "그룹 색상", example = "#FFFFFF")
	String color,

	@Schema(description = "메모", example = "숙소에서 도보 15분 정도")
	String memo,

	@Schema(description = "장소 블록 ID", example = "[1, 2, 3]")
	List<Long> placeBlockIds
) {
	public static PlaceGroupResponse of(PlaceGroup placeGroup, List<Long> placeBlockIds) {
		return new PlaceGroupResponse(
			placeGroup.getId(),
			placeGroup.getName(),
			placeGroup.getColor(),
			placeGroup.getMemo(),
			placeBlockIds
		);
	}
}
