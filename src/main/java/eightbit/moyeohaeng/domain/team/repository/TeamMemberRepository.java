package eightbit.moyeohaeng.domain.team.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import eightbit.moyeohaeng.domain.team.entity.TeamMember;

@Repository
public interface TeamMemberRepository extends JpaRepository<TeamMember, Long> {
	boolean existsByTeam_IdAndMember_Id(Long teamId, Long memberId);
}
