package eightbit.moyeohaeng.domain.team.dto.response;

import eightbit.moyeohaeng.domain.team.dto.TeamDto;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record CreationTeamResponseDto(
	@NotNull
	TeamDto newTeam
) {
}
