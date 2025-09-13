package eightbit.moyeohaeng.domain.team.service;

import java.util.List;

import eightbit.moyeohaeng.domain.member.dto.MemberDto;
import eightbit.moyeohaeng.domain.team.dto.TeamDto;
import eightbit.moyeohaeng.domain.team.dto.response.InviteMemberResponseDto;
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

	/**
	 * @param teamId          초대가 이루어질 팀의 ID
	 * @param inviterMemberId 초대하는 회원(초대자)의 ID
	 * @param inviteeMemberId 초대받는 회원(피초대자)의 ID
	 */
	InviteMemberResponseDto inviteMember(Long teamId, Long inviterMemberId, Long inviteeMemberId);
}
