package eightbit.moyeohaeng.domain.selection.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import eightbit.moyeohaeng.domain.member.common.exception.MemberErrorCode;
import eightbit.moyeohaeng.domain.member.common.exception.MemberException;
import eightbit.moyeohaeng.domain.member.entity.member.Member;
import eightbit.moyeohaeng.domain.member.repository.MemberRepository;
import eightbit.moyeohaeng.domain.selection.common.exception.PlaceBlockErrorCode;
import eightbit.moyeohaeng.domain.selection.common.exception.PlaceBlockException;
import eightbit.moyeohaeng.domain.selection.dto.request.PlaceBlockCommentCreateRequest;
import eightbit.moyeohaeng.domain.selection.dto.request.PlaceBlockCommentUpdateRequest;
import eightbit.moyeohaeng.domain.selection.dto.response.PlaceBlockCommentResponse;
import eightbit.moyeohaeng.domain.selection.dto.response.PlaceBlockCommentSummaryResponse;
import eightbit.moyeohaeng.domain.selection.entity.PlaceBlock;
import eightbit.moyeohaeng.domain.selection.entity.PlaceBlockComment;
import eightbit.moyeohaeng.domain.selection.repository.PlaceBlockCommentRepository;
import eightbit.moyeohaeng.domain.selection.repository.PlaceBlockRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PlaceBlockCommentService {

	private final PlaceBlockCommentRepository commentRepository;
	private final PlaceBlockRepository placeBlockRepository;
	private final MemberRepository memberRepository;

	/**
	 * 댓글 생성
	 */
	@Transactional
	public PlaceBlockCommentResponse create(Long projectId, Long placeBlockId, Long memberId,
		PlaceBlockCommentCreateRequest request) {

		Member member = getMember(memberId);
		PlaceBlock placeBlock = getPlaceBlock(projectId, placeBlockId);

		PlaceBlockComment comment = PlaceBlockComment.builder()
			.content(request.content())
			.member(member)
			.placeBlock(placeBlock)
			.build();

		commentRepository.save(comment);

		return PlaceBlockCommentResponse.of(comment);
	}

	/**
	 * 댓글 수정
	 */
	@Transactional
	public PlaceBlockCommentResponse update(Long projectId, Long placeBlockId, Long commentId, Long memberId,
		PlaceBlockCommentUpdateRequest request) {

		Member member = getMember(memberId);
		PlaceBlock placeBlock = getPlaceBlock(projectId, placeBlockId);

		PlaceBlockComment comment = commentRepository.findByIdAndPlaceBlockAndDeletedAtIsNull(commentId, placeBlock)
			.orElseThrow(() -> new PlaceBlockException(PlaceBlockErrorCode.PLACE_BLOCK_NOT_FOUND));

		// 권한 확인
		if (!comment.getMember().getId().equals(member.getId())) {
			throw new PlaceBlockException(PlaceBlockErrorCode.FORBIDDEN);
		}

		comment.updateContent(request.content());

		return PlaceBlockCommentResponse.of(comment);
	}

	/**
	 * 댓글 삭제
	 */
	@Transactional
	public void delete(Long projectId, Long placeBlockId, Long commentId, Long memberId) {

		Member member = getMember(memberId);
		PlaceBlock placeBlock = getPlaceBlock(projectId, placeBlockId);

		PlaceBlockComment comment = commentRepository.findByIdAndPlaceBlockAndDeletedAtIsNull(commentId, placeBlock)
			.orElseThrow(() -> new PlaceBlockException(PlaceBlockErrorCode.PLACE_BLOCK_NOT_FOUND));

		if (!comment.getMember().getId().equals(member.getId())) {
			throw new PlaceBlockException(PlaceBlockErrorCode.FORBIDDEN);
		}

		commentRepository.delete(comment);
	}

	/**
	 * 댓글 전체 조회
	 */
	public List<PlaceBlockCommentResponse> getComments(Long projectId, Long placeBlockId) {

		PlaceBlock placeBlock = getPlaceBlock(projectId, placeBlockId);

		return commentRepository.findAllByPlaceBlockAndDeletedAtIsNullOrderByCreatedAtAsc(placeBlock)
			.stream()
			.map(PlaceBlockCommentResponse::of)
			.toList();
	}

	/**
	 * 댓글 요약 조회 (총 개수 + 마지막 댓글)
	 */
	public PlaceBlockCommentSummaryResponse getCommentSummary(Long projectId, Long placeBlockId) {

		PlaceBlock placeBlock = getPlaceBlock(projectId, placeBlockId);

		int totalCount = commentRepository.countByPlaceBlockAndDeletedAtIsNull(placeBlock).intValue();

		return commentRepository.findTop1ByPlaceBlockAndDeletedAtIsNullOrderByCreatedAtDesc(placeBlock)
			.map(last -> new PlaceBlockCommentSummaryResponse(
				totalCount,
				new PlaceBlockCommentSummaryResponse.LastComment(last.getContent(), last.getMember().getName())
			))
			.orElse(new PlaceBlockCommentSummaryResponse(totalCount, null));
	}
	
	private Member getMember(Long memberId) {
		return memberRepository.findById(memberId)
			.orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));
	}

	private PlaceBlock getPlaceBlock(Long projectId, Long placeBlockId) {
		return placeBlockRepository.findByIdAndProjectId(placeBlockId, projectId)
			.orElseThrow(() -> new PlaceBlockException(PlaceBlockErrorCode.PLACE_BLOCK_NOT_FOUND));
	}
}
