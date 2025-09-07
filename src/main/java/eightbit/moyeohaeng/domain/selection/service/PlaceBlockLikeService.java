package eightbit.moyeohaeng.domain.selection.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import eightbit.moyeohaeng.domain.member.common.exception.MemberErrorCode;
import eightbit.moyeohaeng.domain.member.common.exception.MemberException;
import eightbit.moyeohaeng.domain.member.entity.member.Member;
import eightbit.moyeohaeng.domain.member.repository.MemberRepository;
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
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PlaceBlockLikeService {

	private final PlaceBlockLikeRepository placeBlockLikeRepository;
	private final PlaceBlockRepository placeBlockRepository;
	private final MemberRepository memberRepository;

	/**
	 * 장소 블록 좋아요 토글
	 *
	 * @param projectId    프로젝트 ID
	 * @param placeBlockId 장소 블록 ID
	 * @param memberId     좋아요 요청한 회원 ID
	 * @return 최신 좋아요 요약 응답 DTO
	 */
	@Transactional
	@ProjectEvent(eventType = EventType.PLACE_BLOCK_LIKE, actionType = ActionType.UPDATED)
	public PlaceBlockLikeSummaryResponse toggleLike(@ProjectId Long projectId, Long placeBlockId, Long memberId) {
		Member member = memberRepository.findById(memberId)
			.orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));

		PlaceBlock placeBlock = placeBlockRepository.findByIdAndProjectId(placeBlockId, projectId)
			.orElseThrow(() -> new PlaceBlockException(PlaceBlockErrorCode.PLACE_BLOCK_NOT_FOUND));

		return placeBlockLikeRepository.findByMemberAndPlaceBlockAndDeletedAtIsNull(member, placeBlock)
			.map(existingLike -> {
				placeBlockLikeRepository.delete(existingLike);
				return buildLikeSummary(placeBlock, member);
			})
			.orElseGet(() -> {
				// 없으면 새로 생성
				PlaceBlockLike newLike = PlaceBlockLike.builder()
					.member(member)
					.placeBlock(placeBlock)
					.build();
				placeBlockLikeRepository.save(newLike);
				return buildLikeSummary(placeBlock, member);
			});
	}

	/**
	 * 장소 블록 좋아요 요약 정보 생성
	 *
	 * @param placeBlock 대상 장소 블록
	 * @param member     요청한 회원
	 * @return PlaceBlockLikeSummaryResponse
	 */
	private PlaceBlockLikeSummaryResponse buildLikeSummary(PlaceBlock placeBlock, Member member) {
		Long totalCount = placeBlockLikeRepository.countByPlaceBlockAndDeletedAtIsNull(placeBlock);
		List<String> likedMembers = placeBlockLikeRepository.findAllByPlaceBlockAndDeletedAtIsNull(placeBlock)
			.stream()
			.map(like -> like.getMember().getEmail())
			.toList();

		boolean liked = placeBlockLikeRepository.findByMemberAndPlaceBlockAndDeletedAtIsNull(member, placeBlock)
			.isPresent();

		return PlaceBlockLikeSummaryResponse.of(totalCount, liked, likedMembers);
	}
}
