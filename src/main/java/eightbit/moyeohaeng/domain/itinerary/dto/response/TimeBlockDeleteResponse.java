package eightbit.moyeohaeng.domain.itinerary.dto.response;

import eightbit.moyeohaeng.domain.itinerary.entity.TimeBlock;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "시간 블록 삭제 응답 DTO")
public record TimeBlockDeleteResponse(
	@Schema(description = "시간 블록 ID", example = "1")
	Long id,

	@Schema(description = "일차", example = "1")
	Integer day
) {
	public static TimeBlockDeleteResponse from(TimeBlock timeBlock) {
		return new TimeBlockDeleteResponse(timeBlock.getId(), timeBlock.getDay());
	}
}
