package eightbit.moyeohaeng.domain.project.dto.response;

import java.time.LocalTime;

import eightbit.moyeohaeng.domain.project.entity.TimeBlock;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "시간 블록 응답 DTO")
public record TimeBlockResponse(
	@Schema(description = "시간 블록 ID", example = "1")
	Long id,

	@Schema(description = "일차", example = "1")
	Integer day,

	@Schema(description = "시작 시간", example = "20:30:00")
	LocalTime startTime,

	@Schema(description = "종료 시간", example = "22:00:00")
	LocalTime endTime,

	@Schema(description = "메모", example = "야경이 아름다운 곳")
	String memo,

	@Schema(description = "장소 블록 ID", example = "1")
	Long placeBlockId
) {
	public static TimeBlockResponse from(TimeBlock timeBlock) {
		return new TimeBlockResponse(
			timeBlock.getId(),
			timeBlock.getDay(),
			timeBlock.getStartTime(),
			timeBlock.getEndTime(),
			timeBlock.getMemo(),
			timeBlock.getPlaceBlock().getId()
		);
	}
}
