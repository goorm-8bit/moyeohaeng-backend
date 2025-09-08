package eightbit.moyeohaeng.domain.team.service;

import java.util.List;

import eightbit.moyeohaeng.domain.team.dto.MemberDto;
import eightbit.moyeohaeng.domain.team.dto.TeamDto;
import eightbit.moyeohaeng.domain.team.entity.Team;
import eightbit.moyeohaeng.domain.team.entity.TeamRole;

public interface TeamService {

	/**
	 * @param teamName = 만들 팀명
	 * @param memberId = 팀을 만드는 사람
	 * @return TeamDto = 실제로 만든 팀
	 */
	TeamDto createTeam(String teamName, Long memberId);

	TeamDto getTeamDto(Long teamId);

	Team getTeam(Long teamId);

	List<TeamDto> getMyTeams(Long memberId);

	List<MemberDto> getTeamMembers(Long teamId, Long memberId);

	TeamRole findMyRole(Long teamId, Long memberId);

	Boolean deleteTeam(Long teamId, Long memberId);

	Boolean checkTeamMember(Long teamId, Long memberId);
}
