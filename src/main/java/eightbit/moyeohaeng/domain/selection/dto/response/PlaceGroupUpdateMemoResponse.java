package eightbit.moyeohaeng.domain.selection.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "장소 그룹 메모 수정 응답 DTO")
public record PlaceGroupUpdateMemoResponse(
	@Schema(description = "장소 그룹 ID", example = "1")
	Long id,

	@Schema(description = "메모", example = "숙소에서 도보 15분 정도")
	String memo
) {
	public static PlaceGroupUpdateMemoResponse of(Long id, String memo) {
		return new PlaceGroupUpdateMemoResponse(id, memo);
	}
}
