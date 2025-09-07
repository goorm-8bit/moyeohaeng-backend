package eightbit.moyeohaeng.domain.selection.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import eightbit.moyeohaeng.domain.place.common.exception.PlaceErrorCode;
import eightbit.moyeohaeng.domain.place.common.exception.PlaceException;
import eightbit.moyeohaeng.domain.place.entity.Place;
import eightbit.moyeohaeng.domain.place.repository.PlaceRepository;
import eightbit.moyeohaeng.domain.project.common.exception.ProjectErrorCode;
import eightbit.moyeohaeng.domain.project.common.exception.ProjectException;
import eightbit.moyeohaeng.domain.project.entity.Project;
import eightbit.moyeohaeng.domain.project.repository.ProjectRepository;
import eightbit.moyeohaeng.domain.selection.common.exception.PlaceBlockErrorCode;
import eightbit.moyeohaeng.domain.selection.common.exception.PlaceBlockException;
import eightbit.moyeohaeng.domain.selection.dto.request.PlaceBlockCreateRequest;
import eightbit.moyeohaeng.domain.selection.dto.request.PlaceBlockUpdateMemoRequest;
import eightbit.moyeohaeng.domain.selection.dto.response.PlaceBlockCreateResponse;
import eightbit.moyeohaeng.domain.selection.dto.response.PlaceBlockDeleteResponse;
import eightbit.moyeohaeng.domain.selection.dto.response.PlaceBlockResponse;
import eightbit.moyeohaeng.domain.selection.dto.response.PlaceBlockUpdateMemoResponse;
import eightbit.moyeohaeng.domain.selection.entity.PlaceBlock;
import eightbit.moyeohaeng.domain.selection.repository.PlaceBlockRepository;
import eightbit.moyeohaeng.global.dto.PageResponse;
import lombok.RequiredArgsConstructor;

/**
 * PlaceBlock 도메인 서비스.
 * 생성/조회/수정/삭제 및 프로젝트 범위 내 권한 검사를 담당한다.
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PlaceBlockService {

	private static final int MAX_PER_PROJECT = 100;

	private final PlaceBlockRepository placeBlockRepository;
	private final ProjectRepository projectRepository;
	private final PlaceRepository placeRepository;

	@Transactional
	public PlaceBlockCreateResponse create(Long projectId, PlaceBlockCreateRequest request) {
		Project project = projectRepository.findById(projectId)
			.orElseThrow(() -> new ProjectException(ProjectErrorCode.PROJECT_NOT_FOUND));

		// 원자적으로 카운터를 증가시킨다. 증가하지 않으면 예외를 발생시킨다.
		int updated = projectRepository.incrementBlockCountIfLessThan(projectId, MAX_PER_PROJECT);
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
	public PlaceBlockUpdateMemoResponse updateMemo(Long projectId, Long placeBlockId,
		PlaceBlockUpdateMemoRequest request) {
		// 장소 블록 조회 및 프로젝트에 속해있는지 검증
		PlaceBlock placeBlock = getPlaceBlock(projectId, placeBlockId);
		placeBlock.updateMemo(request.memo());

		return PlaceBlockUpdateMemoResponse.of(placeBlockId, request.memo());
	}

	/**
	 * 주어진 프로젝트 내의 모든 장소 블록을 페이지로 조회합니다.
	 */
	public PageResponse<PlaceBlockResponse> getPages(Long projectId, Pageable pageable) {
		Page<PlaceBlock> page = placeBlockRepository.findByProjectId(projectId, pageable);
		return PageResponse.from(page, PlaceBlockResponse::from);
	}

	@Transactional
	public PlaceBlockDeleteResponse delete(Long projectId, Long placeBlockId) {
		// 장소 블록 조회 및 프로젝트에 속해있는지 검증
		PlaceBlock placeBlock = getPlaceBlock(projectId, placeBlockId);
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
