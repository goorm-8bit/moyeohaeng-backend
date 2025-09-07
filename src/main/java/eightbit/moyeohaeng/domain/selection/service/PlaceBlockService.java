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
import eightbit.moyeohaeng.domain.selection.dto.request.PlaceBlockUpdateRequest;
import eightbit.moyeohaeng.domain.selection.dto.response.PlaceBlockCreateResponse;
import eightbit.moyeohaeng.domain.selection.dto.response.PlaceBlockResponse;
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

	/**
	 * 주어진 프로젝트 내에서 특정 장소 블록을 조회합니다.
	 */
	public PlaceBlockResponse get(Long projectId, Long placeBlockId) {
		return PlaceBlockResponse.from(getOrThrow(projectId, placeBlockId));
	}

	/**
	 * 주어진 프로젝트 내의 모든 장소 블록을 페이지로 조회합니다.
	 */
	public PageResponse<PlaceBlockResponse> getPages(Long projectId, Pageable pageable) {
		Page<PlaceBlock> page = placeBlockRepository.findByProjectId(projectId, pageable);
		return PageResponse.from(page, PlaceBlockResponse::from);
	}

	/**
	 * 특정 장소 블록의 정보를 수정합니다.
	 * null이 아닌 필드만 업데이트하며, 권한을 검사합니다.
	 */
	@Transactional
	public PlaceBlockResponse update(Long projectId, Long placeBlockId, Long userId, String userRole,
		PlaceBlockUpdateRequest request) {
		if (!canEdit(userRole))
			throw new PlaceBlockException(PlaceBlockErrorCode.FORBIDDEN);

		PlaceBlock placeBlock = getOrThrow(projectId, placeBlockId);

		return PlaceBlockResponse.from(placeBlock);
	}

	/**
	 * 특정 장소 블록을 삭제합니다.
	 * 권한을 검사한 후 소프트 삭제를 실행합니다.
	 */
	@Transactional
	public void delete(Long projectId, Long placeBlockId, Long userId, String userRole) {
		if (!canEdit(userRole))
			throw new PlaceBlockException(PlaceBlockErrorCode.FORBIDDEN);
		PlaceBlock placeBlock = getOrThrow(projectId, placeBlockId);
		placeBlockRepository.delete(placeBlock);
	}

	private PlaceBlock getOrThrow(Long projectId, Long placeBlockId) {
		return placeBlockRepository.findByIdAndProjectId(placeBlockId, projectId)
			.orElseThrow(() -> new PlaceBlockException(PlaceBlockErrorCode.PLACE_BLOCK_NOT_FOUND));
	}

	private boolean canEdit(String role) {
		return "OWNER".equals(role) || "EDITOR".equals(role);
	}
}
