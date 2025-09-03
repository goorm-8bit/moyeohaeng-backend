package eightbit.moyeohaeng.domain.team.service;

import eightbit.moyeohaeng.domain.member.entity.member.Member;
import eightbit.moyeohaeng.domain.member.repository.MemberRepository;
import eightbit.moyeohaeng.domain.team.entity.Team;
import eightbit.moyeohaeng.domain.team.entity.TeamMember;
import eightbit.moyeohaeng.domain.team.entity.TeamRole;
import eightbit.moyeohaeng.domain.team.repository.TeamMemberRepository;
import eightbit.moyeohaeng.domain.team.repository.TeamRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TeamServiceImpl implements TeamService{

	private final TeamRepository teamRepository;
	private final TeamMemberRepository teamMemberRepository;
	private final MemberRepository memberRepository;
	
	@Transactional
	public Team creationTeam(String TeamName, Long memberId) {
		
		Team team = Team.builder()
		                .name(TeamName)
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
		
		return team;
	}
	
	public List<Team> searchMyTeamList(Long memberId) {
		
//		Member member = memberRepository.findById(memberId)
//		                                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));
		
		return teamMemberRepository.findTeamsByMemberId(memberId);
	}
}
