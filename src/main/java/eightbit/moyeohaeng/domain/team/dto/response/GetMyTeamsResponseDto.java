package eightbit.moyeohaeng.domain.team.dto.response;

import java.util.List;

import eightbit.moyeohaeng.domain.team.dto.TeamDto;
import lombok.Builder;

@Builder
public record GetMyTeamsResponseDto(
	Long memberId,
	List<TeamDto> teams
) {
	public static GetMyTeamsResponseDto of(Long memberId, List<TeamDto> teams) {
		return GetMyTeamsResponseDto.builder()
			.memberId(memberId)
			.teams(teams == null ? List.of() : teams)
			.build();
	}
}
