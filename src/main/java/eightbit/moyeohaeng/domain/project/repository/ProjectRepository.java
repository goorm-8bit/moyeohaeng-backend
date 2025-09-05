package eightbit.moyeohaeng.domain.project.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import eightbit.moyeohaeng.domain.project.entity.Project;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

	Optional<Project> findByExternalId(String externalId);

	@Query("SELECT p FROM Project p WHERE p.team.id = :teamId")
	List<Project> findByTeamId(@Param("teamId") Long teamId, Sort sort);

	@Query("SELECT DISTINCT p FROM Project p " +
		"JOIN p.team t " +
		"JOIN TeamMember tm ON tm.team = t " +
		"WHERE tm.member.id = :memberId")
	List<Project> findByMemberId(@Param("memberId") Long memberId, Sort sort);
}
