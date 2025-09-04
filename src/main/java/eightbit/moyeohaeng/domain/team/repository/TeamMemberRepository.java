package eightbit.moyeohaeng.domain.team.repository;

import eightbit.moyeohaeng.domain.member.entity.member.Member;
import eightbit.moyeohaeng.domain.team.entity.Team;
import eightbit.moyeohaeng.domain.team.entity.TeamMember;
import eightbit.moyeohaeng.domain.team.entity.TeamRole;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface TeamMemberRepository extends JpaRepository<TeamMember, Long> {
	
	
	List<TeamMember> findByMember_Id(Long memberId);
	
	@Query("SELECT tm.team FROM TeamMember tm WHERE tm.member.id = :memberId")
	List<Team> findTeamsByMemberId(@Param("memberId") Long memberId);
	
	boolean existsByTeam_IdAndMember_Id(Long teamId, Long memberId);
	
	@Query("""
	          select tm.teamRole
	          from TeamMember tm
	          where tm.team.id = :teamId and tm.member.id = :memberId
	       """)
	Optional<TeamRole> findRoleByTeamIdAndMemberId(@Param("teamId") Long teamId,
	                                               @Param("memberId") Long memberId);
	
	
	@Query("SELECT m FROM TeamMember tm JOIN FETCH tm.member m WHERE tm.team.id = :teamId")
	List<Member> findMembersByTeamId(@Param("teamId") Long teamId);
	
	@Modifying(clearAutomatically = true, flushAutomatically = true)
	@Query("UPDATE TeamMember tm " +
		"SET tm.deletedAt = CURRENT_TIMESTAMP " +
		"WHERE tm.team.id = :teamId AND tm.deletedAt IS NULL")
	int softDeleteAllByTeamId(@Param("teamId") Long teamId);
}
