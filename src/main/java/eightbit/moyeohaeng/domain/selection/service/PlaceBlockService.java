package eightbit.moyeohaeng.domain.selection.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import eightbit.moyeohaeng.domain.place.common.exception.PlaceErrorCode;
import eightbit.moyeohaeng.domain.place.common.exception.PlaceException;
import eightbit.moyeohaeng.domain.place.entity.Place;
import eightbit.moyeohaeng.domain.place.repository.PlaceRepository;
import eightbit.moyeohaeng.domain.project.common.annotation.ActionType;
import eightbit.moyeohaeng.domain.project.common.annotation.EventType;
import eightbit.moyeohaeng.domain.project.common.annotation.ProjectEvent;
import eightbit.moyeohaeng.domain.project.common.exception.ProjectErrorCode;
import eightbit.moyeohaeng.domain.project.common.exception.ProjectException;
import eightbit.moyeohaeng.domain.project.entity.Project;
import eightbit.moyeohaeng.domain.project.repository.ProjectRepository;
import eightbit.moyeohaeng.domain.selection.common.exception.PlaceBlockErrorCode;
import eightbit.moyeohaeng.domain.selection.common.exception.PlaceBlockException;
import eightbit.moyeohaeng.domain.selection.dto.request.PlaceBlockCreateRequest;
import eightbit.moyeohaeng.domain.selection.dto.request.PlaceBlockUpdateMemoRequest;
import eightbit.moyeohaeng.domain.selection.dto.response.PlaceBlockCommentSummary;
import eightbit.moyeohaeng.domain.selection.dto.response.PlaceBlockCreateResponse;
import eightbit.moyeohaeng.domain.selection.dto.response.PlaceBlockDeleteResponse;
import eightbit.moyeohaeng.domain.selection.dto.response.PlaceBlockLikeSummary;
import eightbit.moyeohaeng.domain.selection.dto.response.PlaceBlockResponse;
import eightbit.moyeohaeng.domain.selection.dto.response.PlaceBlockSearchResponse;
import eightbit.moyeohaeng.domain.selection.dto.response.PlaceBlockUpdateMemoResponse;
import eightbit.moyeohaeng.domain.selection.entity.PlaceBlock;
import eightbit.moyeohaeng.domain.selection.repository.PlaceBlockRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PlaceBlockService {

	private static final int MAX_PER_PROJECT = 100;

	private final PlaceBlockRepository placeBlockRepository;
	private final ProjectRepository projectRepository;
	private final PlaceRepository placeRepository;

	@Transactional
	@ProjectEvent(eventType = EventType.PLACE_BLOCK, actionType = ActionType.CREATED)
	public PlaceBlockCreateResponse create(Long projectId, PlaceBlockCreateRequest request) {
		Project project = projectRepository.findById(projectId)
			.orElseThrow(() -> new ProjectException(ProjectErrorCode.PROJECT_NOT_FOUND));

		// 원자적으로 카운터를 증가시킨다. 증가하지 않으면 예외를 발생시킨다.
		int updated = projectRepository.incrementBlockCount(projectId, MAX_PER_PROJECT);
		if (updated == 0) {
			throw new PlaceBlockException(PlaceBlockErrorCode.LIMIT_EXCEEDED, MAX_PER_PROJECT);
		}

		Place place = placeRepository.findById(request.placeId())
			.orElseThrow(() -> new PlaceException(PlaceErrorCode.PLACE_NOT_FOUND));

		PlaceBlock placeBlock = PlaceBlock.of(project, place);

		placeBlockRepository.save(placeBlock);
		return PlaceBlockCreateResponse.of(placeBlock, place);
	}

	@Transactional
	@ProjectEvent(eventType = EventType.PLACE_BLOCK, actionType = ActionType.UPDATED)
	public PlaceBlockUpdateMemoResponse updateMemo(Long projectId, Long placeBlockId,
		PlaceBlockUpdateMemoRequest request) {
		// 장소 블록 조회 및 프로젝트에 속해있는지 검증
		PlaceBlock placeBlock = getPlaceBlock(projectId, placeBlockId);
		placeBlock.updateMemo(request.memo());
		return PlaceBlockUpdateMemoResponse.of(placeBlockId, request.memo());
	}

	public List<PlaceBlockSearchResponse> searchPlaceBlocks(Long projectId, String username) {
		// 장소 블록 조회
		List<PlaceBlockResponse> placeBlocks = placeBlockRepository.findPlaceBlocks(projectId);
		List<Long> placeBlockIds = placeBlocks.stream()
			.map(PlaceBlockResponse::id)
			.toList();

		if (ObjectUtils.isEmpty(placeBlockIds)) {
			return List.of();
		}

		// 좋아요 조회
		Map<Long, PlaceBlockLikeSummary> likes = placeBlockRepository.findPlaceBlockLikes(placeBlockIds, username);

		// 댓글 요약 조회
		Map<Long, PlaceBlockCommentSummary> comments = placeBlockRepository.findPlaceBlockComments(placeBlockIds);

		return placeBlocks.stream()
			.map(placeBlock -> PlaceBlockSearchResponse.of(
				placeBlock,
				likes.getOrDefault(placeBlock.id(), PlaceBlockLikeSummary.empty()),
				comments.getOrDefault(placeBlock.id(), PlaceBlockCommentSummary.empty())
			))
			.toList();
	}

	@Transactional
	@ProjectEvent(eventType = EventType.PLACE_BLOCK, actionType = ActionType.UPDATED)
	public PlaceBlockDeleteResponse delete(Long projectId, Long placeBlockId) {
		// 장소 블록 조회 및 프로젝트에 속해있는지 검증
		PlaceBlock placeBlock = getPlaceBlock(projectId, placeBlockId);

		// 원자적으로 카운터를 감소시킨다. 감소하지 않으면 예외를 발생시킨다.
		int updated = projectRepository.decrementBlockCount(projectId);
		if (updated == 0) {
			throw new PlaceBlockException(PlaceBlockErrorCode.COUNTER_DECREMENT_FAILED);
		}

		placeBlockRepository.delete(placeBlock);
		return PlaceBlockDeleteResponse.of(placeBlockId);
	}

	/**
	 * 장소 블록을 조회하고, 프로젝트에 속한 장소 블록인지 검증 후 반환하는 메서드
	 *
	 * @param projectId 프로젝트 ID
	 * @param placeBlockId 장소 블록 ID
	 * @return 장소 블록
	 */
	private PlaceBlock getPlaceBlock(Long projectId, Long placeBlockId) {
		return placeBlockRepository.findByIdAndProjectId(placeBlockId, projectId)
			.orElseThrow(() -> new PlaceBlockException(PlaceBlockErrorCode.PLACE_BLOCK_NOT_FOUND));
	}
}
