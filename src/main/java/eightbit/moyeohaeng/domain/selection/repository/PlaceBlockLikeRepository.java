package eightbit.moyeohaeng.domain.selection.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import eightbit.moyeohaeng.domain.member.entity.member.Member;
import eightbit.moyeohaeng.domain.selection.entity.PlaceBlock;
import eightbit.moyeohaeng.domain.selection.entity.PlaceBlockLike;

@Repository
public interface PlaceBlockLikeRepository extends JpaRepository<PlaceBlockLike, Long> {

	Optional<PlaceBlockLike> findByMemberAndPlaceBlockAndDeletedAtIsNull(Member member, PlaceBlock placeBlock);

	Long countByPlaceBlockAndDeletedAtIsNull(PlaceBlock placeBlock);

	java.util.List<PlaceBlockLike> findAllByPlaceBlockAndDeletedAtIsNull(PlaceBlock placeBlock);
}
