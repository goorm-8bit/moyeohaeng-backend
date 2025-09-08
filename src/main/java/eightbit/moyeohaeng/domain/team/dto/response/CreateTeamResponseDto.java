package eightbit.moyeohaeng.domain.team.dto.response;

import eightbit.moyeohaeng.domain.team.dto.TeamDto;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record CreateTeamResponseDto(
	@NotNull
	TeamDto newTeam
) {

	public static CreateTeamResponseDto from(TeamDto dto) {
		return new CreateTeamResponseDto(dto);
	}
}
