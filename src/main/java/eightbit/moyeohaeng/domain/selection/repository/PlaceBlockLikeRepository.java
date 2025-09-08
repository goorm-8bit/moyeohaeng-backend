package eightbit.moyeohaeng.domain.selection.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import eightbit.moyeohaeng.domain.selection.entity.PlaceBlock;
import eightbit.moyeohaeng.domain.selection.entity.PlaceBlockLike;

@Repository
public interface PlaceBlockLikeRepository extends JpaRepository<PlaceBlockLike, Long> {

	Optional<PlaceBlockLike> findByAuthorAndPlaceBlockAndDeletedAtIsNull(String author, PlaceBlock placeBlock);

	Optional<PlaceBlockLike> findByAuthorAndPlaceBlock(String author, PlaceBlock placeBlock);

	Long countByPlaceBlockAndDeletedAtIsNull(PlaceBlock placeBlock);

	// 소프트 삭제된 레코드 복구
	@Modifying(clearAutomatically = true, flushAutomatically = true)
	@Query("UPDATE PlaceBlockLike p SET p.deletedAt = null WHERE p.id = :id")
	int restoreById(@Param("id") Long id);

	// 성능 최적화를 위한 이메일 프로젝션
	@Query("SELECT p.author FROM PlaceBlockLike p WHERE p.placeBlock = :placeBlock AND p.deletedAt IS NULL")
	java.util.List<String> findAllEmailsByPlaceBlockAndDeletedAtIsNull(@Param("placeBlock") PlaceBlock placeBlock);
}
