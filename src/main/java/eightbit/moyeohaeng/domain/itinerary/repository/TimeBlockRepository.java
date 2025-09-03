package eightbit.moyeohaeng.domain.itinerary.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import eightbit.moyeohaeng.domain.itinerary.entity.TimeBlock;

@Repository
public interface TimeBlockRepository extends JpaRepository<TimeBlock, Long>, TimeBlockRepositoryCustom {
}
