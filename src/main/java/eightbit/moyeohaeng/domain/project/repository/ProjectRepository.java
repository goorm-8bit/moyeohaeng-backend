package eightbit.moyeohaeng.domain.project.repository;

import eightbit.moyeohaeng.domain.project.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
    Optional<Project> findByExternalId(String externalId);
    List<Project> findAllByTeamId(Long teamId);
}
