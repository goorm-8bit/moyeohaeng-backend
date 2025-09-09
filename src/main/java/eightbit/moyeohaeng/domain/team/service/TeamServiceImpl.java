package eightbit.moyeohaeng.domain.team.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import eightbit.moyeohaeng.domain.member.common.exception.MemberErrorCode;
import eightbit.moyeohaeng.domain.member.common.exception.MemberException;
import eightbit.moyeohaeng.domain.member.entity.member.Member;
import eightbit.moyeohaeng.domain.member.repository.MemberRepository;
import eightbit.moyeohaeng.domain.team.dto.MemberDto;
import eightbit.moyeohaeng.domain.team.dto.TeamDto;
import eightbit.moyeohaeng.domain.team.dto.response.InviteMemberResponseDto;
import eightbit.moyeohaeng.domain.team.entity.Team;
import eightbit.moyeohaeng.domain.team.entity.TeamMember;
import eightbit.moyeohaeng.domain.team.entity.TeamRole;
import eightbit.moyeohaeng.domain.team.exception.TeamErrorCode;
import eightbit.moyeohaeng.domain.team.exception.TeamException;
import eightbit.moyeohaeng.domain.team.repository.TeamMemberRepository;
import eightbit.moyeohaeng.domain.team.repository.TeamRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TeamServiceImpl implements TeamService {

	private final TeamRepository teamRepository;
	private final TeamMemberRepository teamMemberRepository;
	private final MemberRepository memberRepository;

	@Override
	@Transactional
	public InviteMemberResponseDto inviteMember(Long teamId, Long inviterMemberId, Long inviteeMemberId) {

		// 1) 팀 존재 여부
		Team team = teamRepository.findById(teamId)
			.orElseThrow(() -> new TeamException(TeamErrorCode.TEAM_NOT_FOUND));

		// 2) 초대자 & 초대 받을 사람 존재 여부
		Member inviter = memberRepository.findById(inviterMemberId)
			.orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));

		Member invitee = memberRepository.findById(inviteeMemberId)
			.orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));

		// 3) 초대자의 팀 소속 여부 & 초대 권한 여부
		TeamRole teamRole = teamMemberRepository.findRoleByTeamIdAndMemberId(teamId, inviterMemberId)
			.orElseThrow(() -> new TeamException(TeamErrorCode.NOT_HAVE_RIGHT));

		// 권한이 있으면 초대 가능 없으면 예외 발생
		if (teamRole == TeamRole.OWNER) {

			TeamMember teamMember = TeamMember.builder()
				.member(invitee)
				.team(team)
				.teamRole(TeamRole.MEMBER)
				.build();

			TeamMember saved = teamMemberRepository.save(teamMember);

			return InviteMemberResponseDto.builder()
				.teamId(teamId)
				.memberId(inviteeMemberId)
				.build();
			
		} else {
			throw new TeamException(TeamErrorCode.NOT_HAVE_RIGHT);
		}
	}

	// 멤버를 찾아서 teamName 으로 팀 이름을 만들고 만든 사람을 teamMember 이자 OWNER 권한으로 생성
	@Override
	@Transactional
	public TeamDto createTeam(String teamName, Long memberId) {

		Member member = memberRepository.findById(memberId)
			.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

		Team team = Team.builder()
			.name(teamName)
			.build();

		teamRepository.save(team);

		// 생성한 사람이 처음 OWNER 권한
		TeamMember teamMember = TeamMember.builder()
			.member(member)
			.team(team)
			.teamRole(TeamRole.OWNER)
			.build();

		teamMemberRepository.save(teamMember);

		return TeamDto.from(team);
	}

	// 수정 필요함
	// 외부에 teamid 로 팀 조회가 필요한 경우
	@Override
	@Transactional(readOnly = true)
	public TeamDto getTeamDto(Long teamId) {
		// return teamRepository.findById(teamId)
		// 	.map(team -> TeamDto.from(team))
		// 	.orElseThrow(() -> new TeamException(TeamErrorCode.TEAM_NOT_FOUND));

		return TeamDto.from(getTeam(teamId));
	}

	/**
	 * 절때 외부로 직접 나가면 안됨 내부 사용 용도
	 *
	 * @param teamId
	 *
	 * @return Team
	 */
	@Override
	@Transactional(readOnly = true)
	public Team getTeam(Long teamId) {
		return teamRepository.findById(teamId)
			.orElseThrow(() -> new TeamException(TeamErrorCode.TEAM_NOT_FOUND));
	}

	@Override
	@Transactional(readOnly = true)
	public List<TeamDto> getMyTeams(Long memberId) {

		List<Team> teams = teamMemberRepository.findTeamsByMemberId(memberId);

		return teams.stream()
			.map(team -> TeamDto.from(team))
			.toList();
	}

	@Override
	@Transactional(readOnly = true)
	public List<MemberDto> getTeamMembers(Long teamId, Long memberId) {

		boolean b = teamMemberRepository.existsByTeam_IdAndMember_IdAndDeletedAtIsNull(teamId, memberId);

		if (b == true) {
			List<Member> members = teamMemberRepository.findMembersByTeamId(teamId);

			return members.stream()
				.map(member -> MemberDto.from(member))
				.toList();
		} else {
			throw new TeamException(TeamErrorCode.NOT_HAVE_RIGHT);
		}
	}

	@Override
	@Transactional(readOnly = true)
	public TeamRole findMyRole(Long teamId, Long memberId) {

		return teamMemberRepository.findRoleByTeamIdAndMemberId(teamId, memberId).orElseThrow(
			() -> new IllegalArgumentException("TeamMember 에 Role 이 없는 문제 발생")

		);
	}

	@Override
	@Transactional
	public Boolean deleteTeam(Long teamId, Long memberId) {

		TeamRole memberRole = findMyRole(teamId, memberId);

		if (memberRole == TeamRole.OWNER) {
			// 권한 있음

			Long targetTeamId = getTeam(teamId).getId();

			teamMemberRepository.softDeleteAllByTeamId(targetTeamId);
			teamRepository.softDeleteById(targetTeamId);

			return true;
		} else {
			// 권한 없음
			throw new TeamException(TeamErrorCode.NOT_HAVE_RIGHT);
		}
	}

	@Override
	public Boolean checkTeamMember(Long teamId, Long memberId) {

		return teamMemberRepository.existsByTeam_IdAndMember_Id(teamId, memberId);
	}
}
