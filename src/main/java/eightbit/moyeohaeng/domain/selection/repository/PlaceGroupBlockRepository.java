package eightbit.moyeohaeng.domain.selection.repository;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import eightbit.moyeohaeng.domain.selection.entity.PlaceGroupBlock;

@Repository
public interface PlaceGroupBlockRepository extends JpaRepository<PlaceGroupBlock, Long> {
	@Query("SELECT pgb.placeGroup.id FROM PlaceGroupBlock pgb WHERE pgb.placeBlock.id = :placeBlockId")
	List<Long> findByPlaceBlockId(Long placeBlockId);

	void deleteByPlaceGroupIdInAndPlaceBlockId(Collection<Long> placeGroupIds, Long placeBlockId);
}
