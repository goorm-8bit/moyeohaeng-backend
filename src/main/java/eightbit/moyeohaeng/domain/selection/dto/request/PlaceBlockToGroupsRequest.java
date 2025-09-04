package eightbit.moyeohaeng.domain.selection.dto.request;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "장소 그룹에 블록 추가 요청 DTO")
public record PlaceBlockToGroupsRequest(
	@Schema(description = "장소 그룹 ID", example = "[1, 2, 3]")
	List<Long> placeGroupIds
) {
	// 컴팩트 생성자
	public PlaceBlockToGroupsRequest {
		if (placeGroupIds == null) {
			placeGroupIds = List.of();
		}
	}
}
