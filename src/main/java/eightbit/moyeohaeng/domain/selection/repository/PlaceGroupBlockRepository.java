package eightbit.moyeohaeng.domain.selection.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import eightbit.moyeohaeng.domain.selection.entity.PlaceGroupBlock;

@Repository
public interface PlaceGroupBlockRepository extends JpaRepository<PlaceGroupBlock, Long> {
}
