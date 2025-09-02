package eightbit.moyeohaeng.domain.team.repository;

import eightbit.moyeohaeng.domain.team.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {

}
