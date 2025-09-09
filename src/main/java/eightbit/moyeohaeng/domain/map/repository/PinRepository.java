package eightbit.moyeohaeng.domain.map.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import eightbit.moyeohaeng.domain.map.entity.Pin;

@Repository
public interface PinRepository extends JpaRepository<Pin, Long> {
	List<Pin> findAllByProjectId(Long projectId);

	Optional<Pin> findByIdAndProjectId(Long pinId, Long projectId);

	boolean existsByProjectIdAndPlaceId(Long projectId, Long placeId);
}
