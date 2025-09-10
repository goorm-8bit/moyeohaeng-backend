package eightbit.moyeohaeng.domain.project.dto.request;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(name = "ProjectCreateRequest", description = "프로젝트 생성 요청 DTO")
public record ProjectCreateRequest(
	@NotNull
	@Schema(description = "팀 id", example = "1", required = true)
	Long teamId,

	@NotBlank(message = "프로젝트 제목은 필수 입력값입니다.")
	@Size(max = 50, message = "프로젝트 제목은 최대 50자까지 입력 가능합니다.")
	@Schema(description = "프로젝트 제목", example = "제주 맛 집 여행")
	String title,

	@Size(max = 7, message = "색상 코드는 7자까지 입력 가능합니다.")
	@Schema(description = "프로젝트 색상(선택)", example = "#ffffff")
	String color,

	@Schema(description = "시작일(YYYY-MM-DD)", example = "2025-10-01")
	LocalDate startDate,

	@Schema(description = "종료일(YYYY-MM-DD)", example = "2025-10-07")
	LocalDate endDate
) {

	@JsonIgnore
	@AssertTrue(message = "종료일은 시작일과 같거나 이후여야 합니다.")
	public boolean isValidDateRange() {
		return startDate == null || endDate == null || !endDate.isBefore(startDate);
	}

}
