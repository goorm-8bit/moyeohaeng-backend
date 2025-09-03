package eightbit.moyeohaeng.domain.team.repository;

import eightbit.moyeohaeng.domain.team.entity.Team;
import eightbit.moyeohaeng.domain.team.entity.TeamMember;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface TeamMemberRepository extends JpaRepository<TeamMember, Long> {
	
	List<TeamMember> findByMember_Id(Long memberId);
	
	@Query("SELECT tm.team FROM TeamMember tm WHERE tm.member.id = :memberId")
	List<Team> findTeamsByMemberId(@Param("memberId") Long memberId);
	
	boolean existsByTeam_IdAndMember_Id(Long teamId, Long memberId);
	
}
