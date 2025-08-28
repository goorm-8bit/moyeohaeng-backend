package eightbit.moyeohaeng.domain.project.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import eightbit.moyeohaeng.domain.project.entity.PlaceBlock;
import jakarta.persistence.LockModeType;

/**
 * PlaceBlock 엔티티의 저장소.
 * 프로젝트 범위 내 페이지 조회/존재 여부/집계를 제공한다.
 */
public interface PlaceBlockRepository extends JpaRepository<PlaceBlock, Long> {
	Page<PlaceBlock> findByProjectId(Long projectId, Pageable pageable);

	long countByProjectId(Long projectId);

	@Lock(
		LockModeType.PESSIMISTIC_WRITE)
	@Query("select count(p) from PlaceBlock p where p.projectId = :projectId")
	long countByProjectIdWithLock(@Param("projectId") Long projectId);

	boolean existsByIdAndProjectId(Long id, Long projectId);

	Optional<PlaceBlock> findByIdAndProjectId(Long id, Long projectId);
}
