package eightbit.moyeohaeng.domain.selection.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import eightbit.moyeohaeng.domain.selection.entity.PlaceGroup;

@Repository
public interface PlaceGroupRepository extends JpaRepository<PlaceGroup, Long> {
}
