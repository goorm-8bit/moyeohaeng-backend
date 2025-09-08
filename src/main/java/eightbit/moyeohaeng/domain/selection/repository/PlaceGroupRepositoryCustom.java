package eightbit.moyeohaeng.domain.selection.repository;

import java.util.List;

import eightbit.moyeohaeng.domain.selection.dto.response.PlaceGroupResponse;

public interface PlaceGroupRepositoryCustom {

	/**
	 * 프로젝트에 생성된 장소 그룹 목록을 반환한다.
	 *
	 * @param projectId 프로젝트 ID
	 * @return 장소 그룹 목록
	 */
	List<PlaceGroupResponse> findPlaceGroups(Long projectId);
}
