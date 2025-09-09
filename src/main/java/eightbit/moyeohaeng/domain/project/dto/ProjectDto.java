package eightbit.moyeohaeng.domain.project.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import eightbit.moyeohaeng.domain.project.entity.Project;
import eightbit.moyeohaeng.domain.project.entity.ProjectAccess;
import eightbit.moyeohaeng.domain.team.dto.TeamDto;
import lombok.Builder;

public record ProjectDto(
	String externalId,
	String title,
	String color,
	ProjectAccess projectAccess,
	LocalDate startDate,
	LocalDate endDate,
	Integer travelDays,
	@JsonProperty("isAllowGuest") boolean isAllowGuest,
	@JsonProperty("isAllowViewer") boolean isAllowViewer,
	LocalDateTime modifiedAt,
	TeamDto team

) {
	public static ProjectDto from(Project project) {
		return ProjectDto.of(
			project.getExternalId(),
			project.getTitle(),
			project.getColor(),
			project.getProjectAccess(),
			project.getStartDate(),
			project.getEndDate(),
			project.getTravelDays(),
			project.isAllowGuest(),
			project.isAllowViewer(),
			project.getModifiedAt(),
			TeamDto.from(project.getTeam()) // TODO N+1 문제 확인 
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
		String color,
		ProjectAccess projectAccess,
		LocalDate startDate,
		LocalDate endDate,
		Integer travelDays,
		boolean isAllowGuest,
		boolean isAllowViewer,
		LocalDateTime updateAt,
		TeamDto team
	) {
		return new ProjectDto(
			externalId, title, color, projectAccess, startDate, endDate, travelDays, isAllowGuest, isAllowViewer,
			updateAt, team
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

