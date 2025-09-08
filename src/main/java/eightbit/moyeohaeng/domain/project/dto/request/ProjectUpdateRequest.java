package eightbit.moyeohaeng.domain.project.dto.request;

import java.time.LocalDate;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Schema(name = "ProjectUpdateRequest", description = "프로젝트 수정 요청 DTO")
public record ProjectUpdateRequest(
	@NotNull(message = "프로젝트 제목은 null과 blank가 될 수 없습니다..")
	@NotBlank(message = "프로젝트 제목은 null과 blank가 될 수 없습니다.")
	@Size(max = 50, message = "프로젝트 제목은 최대 50자까지 입력 가능합니다.")
	@Schema(description = "프로젝트 제목", example = "제주 맛 집 여행 - 수정")
	String title,

	@Size(max = 7, message = "색상 코드는 7자까지 입력 가능합니다.")
	@Schema(description = "프로젝트 색상(선택)", example = "#ffffff")
	@Pattern(regexp = "^#([0-9a-fA-F]{6}|[0-9a-fA-F]{3})$", message = "유효한 HEX 색상 코드(#RGB 또는 #RRGGBB)여야 합니다.")
	String color,

	@Schema(description = "시작일(YYYY-MM-DD)", example = "2025-10-02")
	LocalDate startDate,

	@Schema(description = "종료일(YYYY-MM-DD)", example = "2025-10-08")
	LocalDate endDate) {

	@AssertTrue(message = "종료일은 시작일과 같거나 이후여야 합니다.")
	public boolean isValidDateRange() {
		return startDate == null || endDate == null || !endDate.isBefore(startDate);
	}

}
