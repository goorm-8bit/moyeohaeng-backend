package eightbit.moyeohaeng.domain.team.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import eightbit.moyeohaeng.domain.team.entity.Team;

@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {

	@Modifying(clearAutomatically = true, flushAutomatically = true)
	@Query("""
		  update Team t
		     set t.deletedAt = CURRENT_TIMESTAMP
		   where t.id = :teamId
		     and t.deletedAt is null
		""")
	int softDeleteById(@Param("teamId") Long teamId);
}
