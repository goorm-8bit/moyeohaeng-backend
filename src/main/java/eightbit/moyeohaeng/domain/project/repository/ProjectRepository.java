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

    Optional<Project> findByExternalIdAndDeletedAtIsNull(String externalId);

    @Query("SELECT p FROM Project p " +
        "JOIN p.team t " +
        "WHERE t.id = :teamId " +
        "AND p.deletedAt IS NULL " +
        "AND t.deletedAt IS NULL")
    List<Project> findActiveByTeamId(@Param("teamId") Long teamId, Sort sort);

    @Query("SELECT DISTINCT p FROM Project p " +
        "JOIN p.team t " +
        "JOIN TeamMember tm ON tm.team = t AND tm.deletedAt IS NULL " +
        "WHERE tm.member.id = :memberId " +
        "AND p.deletedAt IS NULL " +
        "AND t.deletedAt IS NULL")
    List<Project> findByMemberId(@Param("memberId") Long memberId, Sort sort);

    @Query("SELECT p FROM Project p " +
        "WHERE p.id = :projectId " +
        "AND p.deletedAt IS NULL " +
        "AND p.team.deletedAt IS NULL " +
        "AND (p.creator.id = :memberId " +
        "OR EXISTS (" +
        "    SELECT tm FROM TeamMember tm " +
        "    WHERE tm.team.id = p.team.id " +
        "    AND tm.member.id = :memberId " +
        "    AND tm.deletedAt IS NULL" +
        "))")
    Optional<Project> findByIdWithAccessCheck(@Param("projectId") Long projectId, @Param("memberId") Long memberId);

    Optional<Project> findByIdAndDeletedAtIsNull(Long id);
}
