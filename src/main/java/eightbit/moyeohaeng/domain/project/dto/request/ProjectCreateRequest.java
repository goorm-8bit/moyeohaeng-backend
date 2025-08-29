package eightbit.moyeohaeng.domain.project.dto.request;

import java.time.LocalDate;

import eightbit.moyeohaeng.domain.member.entity.member.Member;
import eightbit.moyeohaeng.domain.project.entity.Project;
import eightbit.moyeohaeng.domain.project.entity.ProjectAccess;
import eightbit.moyeohaeng.domain.team.entity.Team;
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

	public Project toEntity(Team team, Member member) {
		return toEntity(team, member, ProjectAccess.PRIVATE);
	}

	public Project toEntity(Team team, Member member, ProjectAccess access) {
		return Project.builder()
			.team(team)
			.creator(member)
			.projectAccess(access)
			.title(title)
			.location(location)
			.startDate(startDate)
			.endDate(endDate)
			.build();
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
