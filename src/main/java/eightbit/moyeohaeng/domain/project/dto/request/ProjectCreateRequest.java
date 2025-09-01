package eightbit.moyeohaeng.domain.project.dto.request;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

public record ProjectCreateRequest(
	@NotNull
	Long teamId,

	@NotBlank(message = "프로젝트 제목은 필수 입력값입니다.")
	@Size(max = 50, message = "프로젝트 제목은 최대 50자까지 입력 가능합니다.")
	String title,

	@Size(max = 50, message = "위치는 최대 50자까지 입력 가능합니다.")
	String location,

	LocalDate startDate,

	LocalDate endDate
) {

	@Builder(builderMethodName = "builder")
	public static ProjectCreateRequest of(
		Long teamId,
		String title,
		String location,
		LocalDate startDate,
		LocalDate endDate
	) {
		return new ProjectCreateRequest(teamId, title, location, startDate, endDate);
	}

	public void validateDates() {
		if (startDate != null && endDate != null && endDate.isBefore(startDate)) {
			throw new IllegalArgumentException("종료일은 시작일보다 이후여야 합니다.");
		}
	}

	public ProjectCreateRequest applyDefaults() {
		return ProjectCreateRequest.of(
			teamId,
			title,
			location,
			startDate != null ? startDate : LocalDate.now(),
			endDate
		);
	}

	public String toStringMasked() {
		return String.format("ProjectCreateRequest(teamId=%d, title=%s, location=%s, start=%s, end=%s)",
			teamId, title, location, startDate, endDate);
	}
}
