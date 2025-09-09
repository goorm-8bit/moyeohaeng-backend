package eightbit.moyeohaeng.domain.selection.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import eightbit.moyeohaeng.domain.member.repository.MemberRepository;
import eightbit.moyeohaeng.domain.project.common.annotation.ActionType;
import eightbit.moyeohaeng.domain.project.common.annotation.EventType;
import eightbit.moyeohaeng.domain.project.common.annotation.ProjectEvent;
import eightbit.moyeohaeng.domain.project.common.annotation.ProjectId;
import eightbit.moyeohaeng.domain.selection.common.exception.PlaceBlockCommentErrorCode;
import eightbit.moyeohaeng.domain.selection.common.exception.PlaceBlockCommentException;
import eightbit.moyeohaeng.domain.selection.common.exception.PlaceBlockErrorCode;
import eightbit.moyeohaeng.domain.selection.common.exception.PlaceBlockException;
import eightbit.moyeohaeng.domain.selection.dto.request.PlaceBlockCommentCreateRequest;
import eightbit.moyeohaeng.domain.selection.dto.request.PlaceBlockCommentUpdateRequest;
import eightbit.moyeohaeng.domain.selection.dto.response.PlaceBlockCommentDeleteResponse;
import eightbit.moyeohaeng.domain.selection.dto.response.PlaceBlockCommentResponse;
import eightbit.moyeohaeng.domain.selection.dto.response.PlaceBlockCommentSummaryResponse;
import eightbit.moyeohaeng.domain.selection.entity.PlaceBlock;
import eightbit.moyeohaeng.domain.selection.entity.PlaceBlockComment;
import eightbit.moyeohaeng.domain.selection.repository.PlaceBlockCommentRepository;
import eightbit.moyeohaeng.domain.selection.repository.PlaceBlockRepository;
import eightbit.moyeohaeng.global.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PlaceBlockCommentService {

	private final PlaceBlockCommentRepository commentRepository;
	private final PlaceBlockRepository placeBlockRepository;
	private final MemberRepository memberRepository;

	private record AuthorView(String name, String profileImageUrl) {
	}

	private AuthorView resolveAuthor(String authorIdentifier) {
		return memberRepository.findByEmail(authorIdentifier)
			.map(m -> new AuthorView(m.getName(), m.getProfileImage()))
			.orElse(new AuthorView("게스트", null));
	}

	private PlaceBlockCommentResponse toResponse(PlaceBlockComment comment) {
		AuthorView authorView = resolveAuthor(comment.getAuthor());
		return PlaceBlockCommentResponse.of(comment, authorView.name(), authorView.profileImageUrl());
	}

	/**
	 * 댓글 생성
	 */
	@Transactional
	@ProjectEvent(eventType = EventType.PLACE_BLOCK_COMMENT, actionType = ActionType.CREATED)
	public PlaceBlockCommentResponse create(
		@ProjectId Long projectId,
		Long placeBlockId,
		CustomUserDetails currentUser,
		PlaceBlockCommentCreateRequest request
	) {
		if (currentUser == null) {
			throw new PlaceBlockCommentException(PlaceBlockCommentErrorCode.FORBIDDEN);
		}
		PlaceBlock placeBlock = getPlaceBlock(projectId, placeBlockId);

		String authorIdentifier = currentUser.getUsername(); // 회원=email, 게스트=uuid
		PlaceBlockComment comment = PlaceBlockComment.of(request.content(), authorIdentifier, placeBlock);
		commentRepository.save(comment);

		return toResponse(comment);
	}

	/**
	 * 댓글 수정
	 */
	@Transactional
	@ProjectEvent(eventType = EventType.PLACE_BLOCK_COMMENT, actionType = ActionType.UPDATED)
	public PlaceBlockCommentResponse update(
		@ProjectId Long projectId,
		Long placeBlockId,
		Long commentId,
		CustomUserDetails currentUser,
		PlaceBlockCommentUpdateRequest request
	) {
		if (currentUser == null) {
			throw new PlaceBlockCommentException(PlaceBlockCommentErrorCode.FORBIDDEN);
		}
		PlaceBlock placeBlock = getPlaceBlock(projectId, placeBlockId);

		PlaceBlockComment comment = commentRepository
			.findByIdAndPlaceBlockAndDeletedAtIsNull(commentId, placeBlock)
			.orElseThrow(() -> new PlaceBlockCommentException(PlaceBlockCommentErrorCode.COMMENT_NOT_FOUND));

		// 소유자 검증: 이메일(email) 또는 게스트 uuid
		if (!comment.getAuthor().equals(currentUser.getUsername())) {
			throw new PlaceBlockCommentException(PlaceBlockCommentErrorCode.FORBIDDEN);
		}

		comment.updateContent(request.content());
		return toResponse(comment);
	}

	/**
	 * 댓글 삭제
	 */
	@Transactional
	@ProjectEvent(eventType = EventType.PLACE_BLOCK_COMMENT, actionType = ActionType.DELETED)
	public PlaceBlockCommentDeleteResponse delete(
		@ProjectId Long projectId,
		Long placeBlockId,
		Long commentId,
		CustomUserDetails currentUser
	) {
		if (currentUser == null) {
			throw new PlaceBlockCommentException(PlaceBlockCommentErrorCode.FORBIDDEN);
		}
		PlaceBlock placeBlock = getPlaceBlock(projectId, placeBlockId);

		PlaceBlockComment comment = commentRepository
			.findByIdAndPlaceBlockAndDeletedAtIsNull(commentId, placeBlock)
			.orElseThrow(() -> new PlaceBlockCommentException(PlaceBlockCommentErrorCode.COMMENT_NOT_FOUND));

		if (!comment.getAuthor().equals(currentUser.getUsername())) {
			throw new PlaceBlockCommentException(PlaceBlockCommentErrorCode.FORBIDDEN);
		}

		commentRepository.delete(comment);
		return PlaceBlockCommentDeleteResponse.from(comment);
	}

	/**
	 * 댓글 전체 조회
	 */
	public List<PlaceBlockCommentResponse> getComments(Long projectId, Long placeBlockId) {
		PlaceBlock placeBlock = getPlaceBlock(projectId, placeBlockId);

		return commentRepository.findAllByPlaceBlockAndDeletedAtIsNullOrderByCreatedAtAsc(placeBlock)
			.stream()
			.map(this::toResponse)
			.toList();
	}

	/**
	 * 댓글 요약 조회 (총 개수 + 마지막 댓글)
	 */
	public PlaceBlockCommentSummaryResponse getCommentSummary(Long projectId, Long placeBlockId) {
		PlaceBlock placeBlock = getPlaceBlock(projectId, placeBlockId);
		int totalCount = commentRepository.countByPlaceBlockAndDeletedAtIsNull(placeBlock).intValue();

		return commentRepository.findTop1ByPlaceBlockAndDeletedAtIsNullOrderByCreatedAtDesc(placeBlock)
			.map(last -> {
				AuthorView authorView = resolveAuthor(last.getAuthor());
				return new PlaceBlockCommentSummaryResponse(
					totalCount,
					new PlaceBlockCommentSummaryResponse.LastComment(last.getContent(), authorView.name())
				);
			})
			.orElse(new PlaceBlockCommentSummaryResponse(totalCount, null));
	}

	private PlaceBlock getPlaceBlock(Long projectId, Long placeBlockId) {
		return placeBlockRepository.findByIdAndProjectId(placeBlockId, projectId)
			.orElseThrow(() -> new PlaceBlockException(PlaceBlockErrorCode.PLACE_BLOCK_NOT_FOUND));
	}
}
