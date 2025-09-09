package eightbit.moyeohaeng.domain.selection.repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import eightbit.moyeohaeng.domain.selection.entity.PlaceBlock;

/**
 * PlaceBlock 엔티티의 저장소.
 * 프로젝트 범위 내 페이지 조회/존재 여부/집계를 제공한다.
 */
@Repository
public interface PlaceBlockRepository extends JpaRepository<PlaceBlock, Long>, PlaceBlockRepositoryCustom {
	Optional<PlaceBlock> findByIdAndProjectId(Long id, Long projectId);

	Optional<PlaceBlock> findByIdAndProjectIdAndDeletedAtIsNull(Long id, Long projectId);

	List<PlaceBlock> findByIdInAndProjectId(Collection<Long> ids, Long projectId);
}
