package eightbit.moyeohaeng.domain.project.dto.request;

import java.time.LocalTime;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(description = "시간 블록 생성 요청 DTO")
public record TimeBlockCreateRequest(
	@Schema(description = "일차", example = "1")
	@NotNull(message = "일차는 필수 입력값입니다.")
	Integer day,

	@Schema(description = "시작 시간", example = "20:30:00")
	LocalTime startTime,

	@Schema(description = "종료 시간", example = "22:00:00")
	LocalTime endTime,

	@Schema(description = "메모", example = "야경이 아름다운 곳")
	@Size(max = 14, message = "메모는 최대 14자까지 입력할 수 있습니다.")
	String memo,

	@Schema(description = "장소 블록 ID", example = "1")
	@NotNull(message = "장소 블록 ID는 필수 입력값입니다.")
	Long placeBlockId
) {

	@AssertTrue(message = "종료 시간은 시작 시간보다 이후여야 합니다.")
	private boolean isValidTimeRange() {
		if (startTime != null && endTime != null) {
			return startTime.isBefore(endTime);
		}
		return true;
	}
}
