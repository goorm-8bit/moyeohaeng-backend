package eightbit.moyeohaeng.domain.project.dto.request;

import java.time.LocalDate;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(name = "ProjectUpdateRequest", description = "프로젝트 수정 요청 DTO")
public record ProjectUpdateRequest(
	@NotBlank(message = "프로젝트 제목은 필수 입력값입니다.")
	@Size(max = 50, message = "프로젝트 제목은 최대 50자까지 입력 가능합니다.")
	@Schema(description = "프로젝트 제목", example = "제주 맛 집 여행 - 수정")
	String title,

	@Size(max = 50, message = "위치는 최대 50자까지 입력 가능합니다.")
	@Schema(description = "프로젝트 위치(선택)", example = "제주도 제주시")
	String location,

	@Schema(description = "시작일(YYYY-MM-DD)", example = "2025-10-02")
	LocalDate startDate,

	@Schema(description = "종료일(YYYY-MM-DD)", example = "2025-10-08")
	LocalDate endDate) {

	@AssertTrue(message = "종료일은 시작일과 같거나 이후여야 합니다.")
	public boolean isValidDateRange() {
		return startDate == null || endDate == null || !endDate.isBefore(startDate);
	}

}
