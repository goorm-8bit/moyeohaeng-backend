package eightbit.moyeohaeng.domain.selection.service;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import eightbit.moyeohaeng.domain.project.common.annotation.ActionType;
import eightbit.moyeohaeng.domain.project.common.annotation.EventType;
import eightbit.moyeohaeng.domain.project.common.annotation.ProjectEvent;
import eightbit.moyeohaeng.domain.project.common.annotation.ProjectId;
import eightbit.moyeohaeng.domain.selection.common.exception.PlaceBlockErrorCode;
import eightbit.moyeohaeng.domain.selection.common.exception.PlaceBlockException;
import eightbit.moyeohaeng.domain.selection.dto.response.PlaceBlockLikeResponse;
import eightbit.moyeohaeng.domain.selection.entity.PlaceBlock;
import eightbit.moyeohaeng.domain.selection.entity.PlaceBlockLike;
import eightbit.moyeohaeng.domain.selection.repository.PlaceBlockLikeRepository;
import eightbit.moyeohaeng.domain.selection.repository.PlaceBlockRepository;
import eightbit.moyeohaeng.global.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PlaceBlockLikeService {

	private final PlaceBlockLikeRepository placeBlockLikeRepository;
	private final PlaceBlockRepository placeBlockRepository;

	/**
	 * 장소 블록 좋아요 토글
	 *
	 * @param projectId    프로젝트 ID
	 * @param placeBlockId 장소 블록 ID
	 * @return 최신 좋아요 요약 응답 DTO
	 */
	@Transactional
	@ProjectEvent(eventType = EventType.PLACE_BLOCK_LIKE, actionType = ActionType.UPDATED)
	public PlaceBlockLikeResponse toggleLike(
		@ProjectId Long projectId,
		Long placeBlockId,
		CustomUserDetails currentUser
	) {
		String author = currentUser.getUsername(); // email
		PlaceBlock placeBlock = placeBlockRepository.findByIdAndProjectIdAndDeletedAtIsNull(placeBlockId, projectId)
			.orElseThrow(() -> new PlaceBlockException(PlaceBlockErrorCode.PLACE_BLOCK_NOT_FOUND));

		// 1) 활성 좋아요 → 언라이크
		return placeBlockLikeRepository.findByAuthorAndPlaceBlockAndDeletedAtIsNull(author, placeBlock)
			.map(existing -> handleUnlike(existing, placeBlock, author))
			// 2) 비활성(soft-deleted) → 복구
			.orElseGet(() -> placeBlockLikeRepository.findByAuthorAndPlaceBlock(author, placeBlock)
				.map(inactive -> handleRestore(inactive, placeBlock, author))
				// 3) 없으면 새로 INSERT
				.orElseGet(() -> handleInsert(author, placeBlock)));
	}

	private PlaceBlockLikeResponse handleUnlike(PlaceBlockLike like, PlaceBlock placeBlock, String author) {
		placeBlockLikeRepository.delete(like); // soft delete
		return PlaceBlockLikeResponse.of(placeBlock.getId(), author, false);
	}

	private PlaceBlockLikeResponse handleRestore(PlaceBlockLike like, PlaceBlock placeBlock, String author) {
		placeBlockLikeRepository.restoreById(like.getId());
		return PlaceBlockLikeResponse.of(placeBlock.getId(), author, true);
	}

	private PlaceBlockLikeResponse handleInsert(String author, PlaceBlock placeBlock) {
		try {
			placeBlockLikeRepository.save(PlaceBlockLike.of(author, placeBlock));
			return PlaceBlockLikeResponse.of(placeBlock.getId(), author, true);
		} catch (DataIntegrityViolationException ignore) {
			// 동시성으로 이미 생겼다면 liked=true로 간주
			return PlaceBlockLikeResponse.of(placeBlock.getId(), author, true);
		}
	}
}
