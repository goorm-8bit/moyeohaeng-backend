package eightbit.moyeohaeng.domain.itinerary.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "시간 블록 메모 수정 응답 DTO")
public record TimeBlockUpdateMemoResponse(
	@Schema(description = "시간 블록 ID", example = "1")
	Long id,

	@Schema(description = "메모", example = "숙소에서 도보 15분 정도")
	String memo
) {
	public static TimeBlockUpdateMemoResponse of(Long id, String memo) {
		return new TimeBlockUpdateMemoResponse(id, memo);
	}
}
