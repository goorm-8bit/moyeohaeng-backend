package eightbit.moyeohaeng.domain.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import eightbit.moyeohaeng.domain.project.entity.TimeBlock;

@Repository
public interface TimeBlockRepository extends JpaRepository<TimeBlock, Long>, TimeBlockRepositoryCustom {
}
