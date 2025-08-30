package eightbit.moyeohaeng.domain.project.dto.request;

import java.time.LocalDate;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ProjectUpdateRequest(
	@NotBlank(message = "프로젝트 제목은 필수 입력값입니다.")
	@Size(max = 50, message = "프로젝트 제목은 최대 50자까지 입력 가능합니다.")
	String title,

	@Size(max = 50, message = "위치는 최대 50자까지 입력 가능합니다.")
	String location,

	LocalDate startDate,

	LocalDate endDate) {

	@AssertTrue(message = "종료일은 시작일과 같거나 이후여야 합니다.")
	public boolean isValidDateRange() {
		return startDate == null || endDate == null || !endDate.isBefore(startDate);
	}

}
