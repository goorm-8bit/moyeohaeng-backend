package eightbit.moyeohaeng.domain.team.service;

import eightbit.moyeohaeng.domain.member.entity.member.Member;
import eightbit.moyeohaeng.domain.member.repository.MemberRepository;
import eightbit.moyeohaeng.domain.team.dto.MemberDto;
import eightbit.moyeohaeng.domain.team.dto.TeamDto;
import eightbit.moyeohaeng.domain.team.entity.Team;
import eightbit.moyeohaeng.domain.team.entity.TeamMember;
import eightbit.moyeohaeng.domain.team.entity.TeamRole;
import eightbit.moyeohaeng.domain.team.exception.MemberNotHaveRightException;
import eightbit.moyeohaeng.domain.team.exception.TeamNotFoundException;
import eightbit.moyeohaeng.domain.team.repository.TeamMemberRepository;
import eightbit.moyeohaeng.domain.team.repository.TeamRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TeamServiceImpl implements TeamService {
	
	private final TeamRepository       teamRepository;
	private final TeamMemberRepository teamMemberRepository;
	private final MemberRepository     memberRepository;
	
	// 멤버를 찾아서 teamName 으로 팀 이름을 만들고 만든 사람을 teamMember 이자 OWNER 권한으로 생성
	@Transactional
	public TeamDto creationTeam(String teamName, Long memberId) {
		
		Team team = Team.builder()
		                .name(teamName)
		                .build();
		
		teamRepository.save(team);
		
		Member member = memberRepository.findById(memberId)
		                                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));
		
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
	@Transactional(readOnly = true)
	public TeamDto getTeamDto(Long teamId) {
		return teamRepository.findById(teamId)
		                     .map(team -> TeamDto.from(team))
		                     .orElseThrow(() -> new TeamNotFoundException(teamId));
	}
	
	/**
	 * 절때 외부로 직접 나가면 안됨 내부 사용 용도
	 *
	 * @param teamId
	 *
	 * @return Team
	 */
	@Transactional(readOnly = true)
	public Team getTeam(Long teamId) {
		return teamRepository.findById(teamId)
		                     .orElseThrow(() -> new TeamNotFoundException(teamId));
	}
	
	@Transactional(readOnly = true)
	public List<TeamDto> getMyTeams(Long memberId) {
		
		List<Team> teams = teamMemberRepository.findTeamsByMemberId(memberId);
		
		return teams.stream()
		            .map(team -> TeamDto.from(team))
		            .toList();
	}
	
	@Transactional(readOnly = true)
	public List<MemberDto> getTeamMembers(Long teamId) {
		
		List<Member> members = teamMemberRepository.findMembersByTeamId(teamId);
		
		return members.stream()
		              .map(member -> MemberDto.from(member))
		              .toList();
	}
	
	@Transactional(readOnly = true)
	public TeamRole findMyRole(Long teamId, Long memberId) {
		
		return teamMemberRepository.findRoleByTeamIdAndMemberId(teamId, memberId).orElseThrow(
			() -> new IllegalArgumentException("TeamMember 에 Role 이 없는 문제 발생")
		                                                                                     
		                                                                                     );
	}
	
	
	@Transactional
	public boolean deleteTeam(Long teamId, Long memberId) {
		
		TeamRole memberRole = findMyRole(teamId, memberId);
		
		if (memberRole == TeamRole.OWNER) {
			// 권한 있음
			
			Long targetTeamId = getTeam(teamId).getId();
			
			teamMemberRepository.softDeleteAllByTeamId(targetTeamId);
			teamRepository.deleteById(targetTeamId);
			
			return true;
		}
		else {
			// 권한 없음
			throw new MemberNotHaveRightException("팀 삭제 권한이 없습니다.");
		}
	}
}
