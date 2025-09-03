package eightbit.moyeohaeng.domain.team.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import eightbit.moyeohaeng.domain.team.entity.Team;

@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {
}
