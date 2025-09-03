package eightbit.moyeohaeng.domain.team.dto.response;


import eightbit.moyeohaeng.domain.team.dto.TeamDto;
import java.util.List;
import lombok.Builder;

@Builder
public record GetMyTeamsResponseDto(
	List<TeamDto> teams
) {


}
