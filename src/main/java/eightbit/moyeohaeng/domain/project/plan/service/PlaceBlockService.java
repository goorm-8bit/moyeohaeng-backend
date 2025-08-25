package eightbit.moyeohaeng.domain.project.plan.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import eightbit.moyeohaeng.domain.project.plan.dto.request.PlaceBlockCreateRequest;
import eightbit.moyeohaeng.domain.project.plan.dto.request.PlaceBlockUpdateRequest;
import eightbit.moyeohaeng.domain.project.plan.dto.response.PlaceBlockPageResponse;
import eightbit.moyeohaeng.domain.project.plan.dto.response.PlaceBlockResponse;
import eightbit.moyeohaeng.domain.project.plan.entity.PlaceBlock;
import eightbit.moyeohaeng.domain.project.plan.exception.PlaceBlockErrorCode;
import eightbit.moyeohaeng.domain.project.plan.exception.PlaceBlockException;
import eightbit.moyeohaeng.domain.project.plan.repository.PlaceBlockRepository;
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

	private final PlaceBlockRepository repository;

	/**
	 * 새로운 장소 블록을 생성합니다.
	 * 최대 개수(100개) 제한과 사용자 권한을 검사합니다.
	 */
	@Transactional
	public PlaceBlockResponse create(Long projectId, Long userId, String userRole, PlaceBlockCreateRequest req) {
		if (!canEdit(userRole))
			throw new PlaceBlockException(PlaceBlockErrorCode.FORBIDDEN);

		if (repository.countByProjectId(projectId) >= MAX_PER_PROJECT) {
			throw new PlaceBlockException(PlaceBlockErrorCode.LIMIT_EXCEEDED);
		}

		PlaceBlock saved = repository.save(req.toEntity(projectId));
		return PlaceBlockResponse.from(saved);
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
	public PlaceBlockPageResponse list(Long projectId, Pageable pageable) {
		Page<PlaceBlock> page = repository.findByProjectId(projectId, pageable);
		return PlaceBlockPageResponse.from(page);
	}

	/**
	 * 특정 장소 블록의 정보를 수정합니다.
	 * null이 아닌 필드만 업데이트하며, 권한을 검사합니다.
	 */
	@Transactional
	public PlaceBlockResponse update(Long projectId, Long placeBlockId, Long userId, String userRole,
		PlaceBlockUpdateRequest req) {
		if (!canEdit(userRole))
			throw new PlaceBlockException(PlaceBlockErrorCode.FORBIDDEN);

		PlaceBlock pb = getOrThrow(projectId, placeBlockId);

		if (req.name() != null)
			pb.setName(req.name());
		if (req.address() != null)
			pb.setAddress(req.address());
		if (req.latitude() != null)
			pb.setLatitude(req.latitude());
		if (req.longitude() != null)
			pb.setLongitude(req.longitude());
		if (req.memo() != null)
			pb.setMemo(req.memo());
		if (req.date() != null)
			pb.setDate(req.date());
		if (req.time() != null)
			pb.setTime(req.time());
		if (req.reviewLink() != null)
			pb.setReviewLink(req.reviewLink());
		if (req.detailLink() != null)
			pb.setDetailLink(req.detailLink());
		if (req.category() != null)
			pb.setCategory(req.category());
		if (req.color() != null)
			pb.setColor(req.color());
		if (req.author() != null)
			pb.setAuthor(req.author());
		if (req.type() != null)
			pb.setType(req.type());

		repository.save(pb);

		return PlaceBlockResponse.from(pb);
	}

	/**
	 * 특정 장소 블록을 삭제합니다.
	 * 권한을 검사한 후 소프트 삭제를 실행합니다.
	 */
	@Transactional
	public void delete(Long projectId, Long placeBlockId, Long userId, String userRole) {
		if (!canEdit(userRole))
			throw new PlaceBlockException(PlaceBlockErrorCode.FORBIDDEN);
		PlaceBlock pb = getOrThrow(projectId, placeBlockId);
		repository.delete(pb);
	}

	private PlaceBlock getOrThrow(Long projectId, Long placeBlockId) {
		return repository.findByIdAndProjectId(placeBlockId, projectId)
			.orElseThrow(() -> new PlaceBlockException(PlaceBlockErrorCode.PLACE_BLOCK_NOT_FOUND));
	}

	private boolean canEdit(String role) {
		return "OWNER".equals(role) || "EDITOR".equals(role);
	}
}