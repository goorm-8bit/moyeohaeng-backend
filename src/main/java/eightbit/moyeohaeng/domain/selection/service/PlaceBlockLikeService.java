package eightbit.moyeohaeng.domain.selection.service;

import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import eightbit.moyeohaeng.domain.project.common.annotation.ActionType;
import eightbit.moyeohaeng.domain.project.common.annotation.EventType;
import eightbit.moyeohaeng.domain.project.common.annotation.ProjectEvent;
import eightbit.moyeohaeng.domain.project.common.annotation.ProjectId;
import eightbit.moyeohaeng.domain.selection.common.exception.PlaceBlockErrorCode;
import eightbit.moyeohaeng.domain.selection.common.exception.PlaceBlockException;
import eightbit.moyeohaeng.domain.selection.dto.response.PlaceBlockLikeSummaryResponse;
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
	public PlaceBlockLikeSummaryResponse toggleLike(
		@ProjectId Long projectId,
		Long placeBlockId,
		CustomUserDetails currentUser
	) {
		String author = currentUser.getUsername(); // email
		PlaceBlock placeBlock = placeBlockRepository.findByIdAndProjectIdAndDeletedAtIsNull(placeBlockId, projectId)
			.orElseThrow(() -> new PlaceBlockException(PlaceBlockErrorCode.PLACE_BLOCK_NOT_FOUND));

		// 1. 활성 좋아요 → 언라이크
		return placeBlockLikeRepository.findByAuthorAndPlaceBlockAndDeletedAtIsNull(author, placeBlock)
			.map(existingLike -> handleUnlike(existingLike, placeBlock, author))
			// 2. 비활성 좋아요 → 복구
			.orElseGet(() -> placeBlockLikeRepository.findByAuthorAndPlaceBlock(author, placeBlock)
				.map(inactive -> handleRestore(inactive, placeBlock, author))
				// 3. 아예 없음 → 새 INSERT
				.orElseGet(() -> handleInsert(author, placeBlock))
			);
	}

	private PlaceBlockLikeSummaryResponse handleUnlike(PlaceBlockLike like, PlaceBlock placeBlock, String author) {
		placeBlockLikeRepository.delete(like); // soft delete
		return buildLikeSummary(placeBlock, author);
	}

	private PlaceBlockLikeSummaryResponse handleRestore(PlaceBlockLike like, PlaceBlock placeBlock, String author) {
		placeBlockLikeRepository.restoreById(like.getId());
		return buildLikeSummary(placeBlock, author);
	}

	private PlaceBlockLikeSummaryResponse handleInsert(String author, PlaceBlock placeBlock) {
		try {
			placeBlockLikeRepository.save(PlaceBlockLike.of(author, placeBlock));
		} catch (DataIntegrityViolationException ignore) {
			// 동시 요청으로 인한 race condition → 무시하고 요약만 갱신
		}
		return buildLikeSummary(placeBlock, author);
	}

	/**
	 * 장소 블록 좋아요 요약 정보 생성
	 *
	 * @param placeBlock 대상 장소 블록
	 * @return PlaceBlockLikeSummaryResponse
	 */
	private PlaceBlockLikeSummaryResponse buildLikeSummary(PlaceBlock placeBlock, String author) {
		Long totalCount = placeBlockLikeRepository.countByPlaceBlockAndDeletedAtIsNull(placeBlock);
		List<String> likedMembers = placeBlockLikeRepository.findAllEmailsByPlaceBlockAndDeletedAtIsNull(placeBlock);

		boolean liked = placeBlockLikeRepository.findByAuthorAndPlaceBlockAndDeletedAtIsNull(author, placeBlock)
			.isPresent();

		return PlaceBlockLikeSummaryResponse.of(totalCount, liked, likedMembers);
	}
}
