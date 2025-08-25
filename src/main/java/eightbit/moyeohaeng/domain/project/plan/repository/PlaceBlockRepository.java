package eightbit.moyeohaeng.domain.project.plan.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import eightbit.moyeohaeng.domain.project.plan.entity.PlaceBlock;

/**
 * PlaceBlock 엔티티의 저장소.
 * 프로젝트 범위 내 페이지 조회/존재 여부/집계를 제공한다.
 */
public interface PlaceBlockRepository extends JpaRepository<PlaceBlock, Long> {
	/**
	 * 주어진 프로젝트 ID에 해당하는 PlaceBlock 목록을 페이지로 조회한다.
	 */
	Page<PlaceBlock> findByProjectId(Long projectId, Pageable pageable);

	long countByProjectId(Long projectId);

	boolean existsByIdAndProjectId(Long id, Long projectId);

	Optional<PlaceBlock> findByIdAndProjectId(Long id, Long projectId);
}