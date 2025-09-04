package eightbit.moyeohaeng.domain.selection.repository;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import eightbit.moyeohaeng.domain.selection.entity.PlaceGroup;

@Repository
public interface PlaceGroupRepository extends JpaRepository<PlaceGroup, Long>, PlaceGroupRepositoryCustom {
	List<PlaceGroup> findByIdInAndProjectId(Collection<Long> ids, Long projectId);
}
