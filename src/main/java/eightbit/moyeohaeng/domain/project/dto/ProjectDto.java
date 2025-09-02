package eightbit.moyeohaeng.domain.project.dto;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

import eightbit.moyeohaeng.domain.project.entity.Project;
import eightbit.moyeohaeng.domain.project.entity.ProjectAccess;
import lombok.Builder;

public record ProjectDto(
	String externalId,
	String title,
	String location,
	ProjectAccess projectAccess,
	LocalDate startDate,
	LocalDate endDate
) {
	public static ProjectDto from(Project project) {
		return ProjectDto.of(
			project.getExternalId(),
			project.getTitle(),
			project.getLocation(),
			project.getProjectAccess(),
			project.getStartDate(),
			project.getEndDate()
		);
	}

	public static List<ProjectDto> from(List<Project> projects) {
		return projects.stream()
			.map(ProjectDto::from)
			.toList();
	}

	@Builder(builderMethodName = "builder")
	public static ProjectDto of(
		String externalId,
		String title,
		String location,
		ProjectAccess projectAccess,
		LocalDate startDate,
		LocalDate endDate
	) {
		return new ProjectDto(
			externalId, title, location, projectAccess, startDate, endDate
		);
	}

	public boolean isActive() {
		LocalDate today = LocalDate.now();
		return (startDate == null || !today.isBefore(startDate)) &&
			(endDate == null || !today.isAfter(endDate));
	}

	public boolean isFinished() {
		return endDate != null && endDate.isBefore(LocalDate.now());
	}

	public boolean isUpcoming() {
		return startDate != null && startDate.isAfter(LocalDate.now());
	}

	public long getDurationDays() {
		return (startDate != null && endDate != null)
			? ChronoUnit.DAYS.between(startDate, endDate)
			: 0;
	}

	public long getRemainingDays() {
		return (endDate != null)
			? ChronoUnit.DAYS.between(LocalDate.now(), endDate)
			: 0;
	}

	public boolean isPublic() {
		return ProjectAccess.PUBLIC.equals(projectAccess);
	}

	public String toStringFormatted() {
		return String.format("[%s] %s (%s ~ %s)",
			externalId, title, startDate, endDate);
	}
}

