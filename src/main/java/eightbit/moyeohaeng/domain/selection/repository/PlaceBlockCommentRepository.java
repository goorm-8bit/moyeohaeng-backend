package eightbit.moyeohaeng.domain.selection.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import eightbit.moyeohaeng.domain.selection.entity.PlaceBlock;
import eightbit.moyeohaeng.domain.selection.entity.PlaceBlockComment;

@Repository
public interface PlaceBlockCommentRepository extends JpaRepository<PlaceBlockComment, Long> {

	List<PlaceBlockComment> findAllByPlaceBlockAndDeletedAtIsNullOrderByCreatedAtAsc(PlaceBlock placeBlock);

	Optional<PlaceBlockComment> findByIdAndPlaceBlockAndDeletedAtIsNull(Long id, PlaceBlock placeBlock);

	Long countByPlaceBlockAndDeletedAtIsNull(PlaceBlock placeBlock);

	Optional<PlaceBlockComment> findTop1ByPlaceBlockAndDeletedAtIsNullOrderByCreatedAtDesc(PlaceBlock placeBlock);
}
