package eightbit.moyeohaeng.domain.itinerary.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;

@Schema(description = "시간 블록 메모 수정 요청 DTO")
public record TimeBlockUpdateMemoRequest(
	@Schema(description = "메모", example = "숙소에서 도보 15분 정도")
	@Size(max = 14, message = "메모는 최대 14자까지 입력할 수 있습니다.")
	String memo
) {
}
