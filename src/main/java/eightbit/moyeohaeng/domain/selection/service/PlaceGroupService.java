package eightbit.moyeohaeng.domain.selection.service;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import eightbit.moyeohaeng.domain.project.common.exception.ProjectErrorCode;
import eightbit.moyeohaeng.domain.project.common.exception.ProjectException;
import eightbit.moyeohaeng.domain.project.entity.Project;
import eightbit.moyeohaeng.domain.project.repository.ProjectRepository;
import eightbit.moyeohaeng.domain.selection.common.exception.PlaceBlockErrorCode;
import eightbit.moyeohaeng.domain.selection.common.exception.PlaceBlockException;
import eightbit.moyeohaeng.domain.selection.common.exception.PlaceGroupErrorCode;
import eightbit.moyeohaeng.domain.selection.common.exception.PlaceGroupException;
import eightbit.moyeohaeng.domain.selection.dto.request.PlaceBlockToGroupsRequest;
import eightbit.moyeohaeng.domain.selection.dto.request.PlaceGroupRequest;
import eightbit.moyeohaeng.domain.selection.dto.request.PlaceGroupUpdateMemoRequest;
import eightbit.moyeohaeng.domain.selection.dto.response.PlaceGroupBlockResponse;
import eightbit.moyeohaeng.domain.selection.dto.response.PlaceGroupResponse;
import eightbit.moyeohaeng.domain.selection.dto.response.PlaceGroupUpdateMemoResponse;
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
	private final ProjectRepository projectRepository;

	@Transactional
	public PlaceGroupResponse create(Long projectId, PlaceGroupRequest request) {
		Project project = projectRepository.findById(projectId)
			.orElseThrow(() -> new ProjectException(ProjectErrorCode.PROJECT_NOT_FOUND));

		// 장소 블록 목록 조회 및 프로젝트에 속해있는지 검증
		List<PlaceBlock> placeBlocks = getPlaceBlocks(projectId, request.placeBlockIds());

		// 장소 그룹 생성
		PlaceGroup placeGroup = PlaceGroup.of(request.name(), request.color(), project);
		placeGroupRepository.save(placeGroup);

		// 장소 그룹에 장소 블록 추가
		if (!request.placeBlockIds().isEmpty()) {
			addPlaceBlocksToGroups(List.of(placeGroup), placeBlocks);
		}

		return PlaceGroupResponse.of(placeGroup, request.placeBlockIds());
	}

	@Transactional
	public PlaceGroupBlockResponse updatePlaceBlockToGroups(Long projectId, Long placeBlockId,
		PlaceBlockToGroupsRequest request) {
		// 장소 블록 조회 및 프로젝트에 속해있는지 검증
		PlaceBlock placeBlock = getPlaceBlock(projectId, placeBlockId);

		// 장소 블록이 속한 그룹 ID 조회
		List<Long> placeGroupIds = placeGroupBlockRepository.findPlaceGroupIdByPlaceBlockId(placeBlockId);

		// 추가해야 하는 그룹
		Set<Long> addGroupIds = new HashSet<>(request.placeGroupIds());
		placeGroupIds.forEach(addGroupIds::remove);

		if (!addGroupIds.isEmpty()) {
			List<PlaceGroup> placeGroups = getPlaceGroups(projectId, addGroupIds);
			addPlaceBlocksToGroups(placeGroups, List.of(placeBlock));
		}

		// 삭제해야 하는 그룹
		Set<Long> deleteGroupIds = new HashSet<>(placeGroupIds);
		request.placeGroupIds().forEach(deleteGroupIds::remove);

		if (!deleteGroupIds.isEmpty()) {
			placeGroupBlockRepository.deleteByPlaceGroupIdInAndPlaceBlockId(deleteGroupIds, placeBlockId);
		}

		return PlaceGroupBlockResponse.of(placeBlockId, request.placeGroupIds());
	}

	@Transactional
	public PlaceGroupResponse update(Long projectId, Long placeGroupId, PlaceGroupRequest request) {
		// 장소 그룹 조회 및 프로젝트에 속해있는지 검증
		PlaceGroup placeGroup = getPlaceGroup(projectId, placeGroupId);
		placeGroup.update(request.name(), request.color());

		// 장소 그룹에 속한 블록 ID 조회
		List<Long> placeBlockIds = placeGroupBlockRepository.findPlaceBlockIdByPlaceGroupId(placeGroupId);

		// 추가해야 하는 블록
		Set<Long> addBlockIds = new HashSet<>(request.placeBlockIds());
		placeBlockIds.forEach(addBlockIds::remove);

		if (!addBlockIds.isEmpty()) {
			List<PlaceBlock> placeBlocks = getPlaceBlocks(projectId, addBlockIds);
			addPlaceBlocksToGroups(List.of(placeGroup), placeBlocks);
		}

		// 삭제해야 하는 블록
		Set<Long> deleteBlockIds = new HashSet<>(placeBlockIds);
		request.placeBlockIds().forEach(deleteBlockIds::remove);

		if (!deleteBlockIds.isEmpty()) {
			placeGroupBlockRepository.deleteByPlaceGroupIdAndPlaceBlockIdIn(placeGroupId, deleteBlockIds);
		}

		return PlaceGroupResponse.of(placeGroup, request.placeBlockIds());
	}

	@Transactional
	public PlaceGroupUpdateMemoResponse updateMemo(Long projectId, Long placeGroupId,
		PlaceGroupUpdateMemoRequest request) {
		// 장소 그룹 조회 및 프로젝트에 속해있는지 검증
		PlaceGroup placeGroup = getPlaceGroup(projectId, placeGroupId);
		placeGroup.updateMemo(request.memo());

		return PlaceGroupUpdateMemoResponse.of(placeGroupId, request.memo());
	}

	public List<PlaceGroupResponse> getPlaceGroups(Long projectId) {
		return placeGroupRepository.findPlaceGroups(projectId);
	}

	/**
	 * 장소 블록을 조회하고, 프로젝트에 속한 장소 블록인지 검증 후 반환하는 메서드
	 *
	 * @param projectId    프로젝트 ID
	 * @param placeBlockId 장소 블록 ID
	 * @return 장소 블록
	 */
	private PlaceBlock getPlaceBlock(Long projectId, Long placeBlockId) {
		return getPlaceBlocks(projectId, List.of(placeBlockId)).getFirst();
	}

	/**
	 * 장소 블록을 조회하고, 프로젝트에 속한 장소 블록인지 검증 후 반환하는 메서드
	 *
	 * @param projectId     프로젝트 ID
	 * @param placeBlockIds 장소 블록 ID
	 * @return 장소 블록 목록
	 */
	private List<PlaceBlock> getPlaceBlocks(Long projectId, Collection<Long> placeBlockIds) {
		List<PlaceBlock> placeBlocks = placeBlockRepository.findByIdInAndProjectId(placeBlockIds, projectId);
		if (placeBlocks.size() != placeBlockIds.size()) {
			throw new PlaceBlockException(PlaceBlockErrorCode.PLACE_BLOCK_NOT_FOUND);
		}
		return placeBlocks;
	}

	/**
	 * 장소 그룹을 조회하고, 프로젝트에 속한 장소 그룹인지 검증 후 반환하는 메서드
	 *
	 * @param projectId    프로젝트 Id
	 * @param placeGroupId 장소 그룹 ID
	 * @return 장소 그룹
	 */
	private PlaceGroup getPlaceGroup(Long projectId, Long placeGroupId) {
		return getPlaceGroups(projectId, List.of(placeGroupId)).getFirst();
	}

	/**
	 * 장소 그룹을 조회하고, 프로젝트에 속한 장소 그룹인지 검증 후 반환하는 메서드
	 *
	 * @param projectId     프로젝트 Id
	 * @param placeGroupIds 장소 그룹 ID
	 * @return 장소 그룹 목록
	 */
	private List<PlaceGroup> getPlaceGroups(Long projectId, Collection<Long> placeGroupIds) {
		List<PlaceGroup> placeGroups = placeGroupRepository.findByIdInAndProjectId(placeGroupIds, projectId);
		if (placeGroups.size() != placeGroupIds.size()) {
			throw new PlaceGroupException(PlaceGroupErrorCode.PLACE_GROUP_NOT_FOUND);
		}
		return placeGroups;
	}

	/**
	 * 장소 블록을 장소 그룹에 추가하는 메서드
	 *
	 * @param placeGroups 장소 블록
	 * @param placeBlocks 장소 그룹
	 */
	private void addPlaceBlocksToGroups(Collection<PlaceGroup> placeGroups, Collection<PlaceBlock> placeBlocks) {
		List<PlaceGroupBlock> placeGroupBlocks = placeGroups.stream()
			.flatMap(placeGroup -> placeBlocks.stream()
				.map(placeBlock -> PlaceGroupBlock.of(placeGroup, placeBlock)))
			.toList();

		placeGroupBlockRepository.saveAll(placeGroupBlocks);
	}
}
