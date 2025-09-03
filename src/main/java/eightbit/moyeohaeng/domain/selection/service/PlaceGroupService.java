package eightbit.moyeohaeng.domain.selection.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import eightbit.moyeohaeng.domain.selection.common.exception.PlaceBlockErrorCode;
import eightbit.moyeohaeng.domain.selection.common.exception.PlaceBlockException;
import eightbit.moyeohaeng.domain.selection.dto.request.PlaceGroupCreateRequest;
import eightbit.moyeohaeng.domain.selection.dto.response.PlaceGroupResponse;
import eightbit.moyeohaeng.domain.selection.entity.PlaceBlock;
import eightbit.moyeohaeng.domain.selection.entity.PlaceGroup;
import eightbit.moyeohaeng.domain.selection.entity.PlaceGroupBlock;
import eightbit.moyeohaeng.domain.selection.repository.PlaceBlockRepository;
import eightbit.moyeohaeng.domain.selection.repository.PlaceGroupBlockRepository;
import eightbit.moyeohaeng.domain.selection.repository.PlaceGroupRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PlaceGroupService {

	private final PlaceGroupRepository placeGroupRepository;
	private final PlaceGroupBlockRepository placeGroupBlockRepository;
	private final PlaceBlockRepository placeBlockRepository;

	@Transactional
	public PlaceGroupResponse create(Long projectId, PlaceGroupCreateRequest request) {
		List<PlaceBlock> placeBlocks = getPlaceBlocks(projectId, request.placeBlockIds());

		// 장소 그룹 생성
		PlaceGroup placeGroup = PlaceGroup.of(request.name(), request.color());
		placeGroupRepository.save(placeGroup);

		// 장소 그룹에 장소 블록 추가
		addPlaceBlocksToGroup(placeGroup, placeBlocks);

		return PlaceGroupResponse.of(placeGroup, request.placeBlockIds());
	}

	private List<PlaceBlock> getPlaceBlocks(Long projectId, List<Long> placeBlockIds) {
		List<PlaceBlock> placeBlocks = placeBlockRepository.findByIdInAndProjectId(placeBlockIds, projectId);
		if (placeBlocks.size() != placeBlockIds.size()) {
			throw new PlaceBlockException(PlaceBlockErrorCode.PLACE_BLOCK_NOT_FOUND);
		}
		return placeBlocks;
	}

	private void addPlaceBlocksToGroup(PlaceGroup placeGroup, List<PlaceBlock> placeBlocks) {
		List<PlaceGroupBlock> placeGroupBlocks = placeBlocks.stream()
			.map(placeBlock -> PlaceGroupBlock.of(placeGroup, placeBlock))
			.toList();

		placeGroupBlockRepository.saveAll(placeGroupBlocks);
	}
}
