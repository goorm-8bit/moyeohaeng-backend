package eightbit.moyeohaeng.domain.team.dto;

import eightbit.moyeohaeng.domain.team.entity.Team;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;


@Builder
public record TeamDto(
	@NotNull
	Long teamId,
	@NotBlank
	String teamName
) {

	public static TeamDto teamDto(Team team) {
		return TeamDto.builder()
		              .teamId(team.getId())
		              .teamName(team.getName())
		              .build();
	}
}
