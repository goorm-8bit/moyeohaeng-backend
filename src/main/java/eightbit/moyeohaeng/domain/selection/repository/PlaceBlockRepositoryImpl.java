package eightbit.moyeohaeng.domain.selection.repository;

import static eightbit.moyeohaeng.domain.member.entity.member.QMember.*;
import static eightbit.moyeohaeng.domain.place.entity.QPlace.*;
import static eightbit.moyeohaeng.domain.selection.entity.QPlaceBlock.*;
import static eightbit.moyeohaeng.domain.selection.entity.QPlaceBlockComment.*;
import static eightbit.moyeohaeng.domain.selection.entity.QPlaceBlockLike.*;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLSubQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import eightbit.moyeohaeng.domain.selection.dto.response.PlaceBlockCommentSummary;
import eightbit.moyeohaeng.domain.selection.dto.response.PlaceBlockLastComment;
import eightbit.moyeohaeng.domain.selection.dto.response.PlaceBlockLikeSummary;
import eightbit.moyeohaeng.domain.selection.dto.response.PlaceBlockResponse;
import eightbit.moyeohaeng.domain.selection.dto.response.QPlaceBlockLastComment;
import eightbit.moyeohaeng.domain.selection.dto.response.QPlaceBlockResponse;
import eightbit.moyeohaeng.domain.selection.entity.QPlaceBlockComment;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class PlaceBlockRepositoryImpl implements PlaceBlockRepositoryCustom {

	private final JPAQueryFactory queryFactory;

	@Override
	public List<PlaceBlockResponse> findPlaceBlocks(Long projectId) {
		// 장소 블록 조회
		return queryFactory
			.select(new QPlaceBlockResponse(
				placeBlock.id,
				place.name,
				place.address,
				place.latitude,
				place.longitude,
				place.detailLink,
				place.category,
				placeBlock.memo,
				placeBlock.createdAt
			))
			.from(placeBlock)
			.join(placeBlock.place, place)
			.where(
				placeBlock.deletedAt.isNull(),
				placeBlock.project.id.eq(projectId)
			)
			.orderBy(placeBlock.createdAt.desc())
			.fetch();
	}

	@Override
	public Map<Long, PlaceBlockLikeSummary> findPlaceBlockLikes(List<Long> placeBlockIds, String username) {
		// 좋아요를 누른 사용자 목록 조회
		Map<Long, List<String>> likedMemberMap = queryFactory
			.select(
				placeBlockLike.placeBlock.id,
				member.email // TODO: author로 변경
			)
			.from(placeBlockLike)
			.join(placeBlockLike.member, member)
			.where(
				placeBlockLike.deletedAt.isNull(),
				placeBlockLike.placeBlock.id.in(placeBlockIds)
			)
			.fetch()
			.stream()
			.collect(Collectors.groupingBy(
				tuple -> Objects.requireNonNull(tuple.get(placeBlockLike.placeBlock.id)),
				Collectors.mapping(
					tuple -> tuple.get(member.email),
					Collectors.toList()
				)
			));

		return likedMemberMap.entrySet().stream()
			.collect(Collectors.toMap(
				Map.Entry::getKey,
				entry -> {
					// 좋아요를 누른 사용자 목록
					List<String> likedMembers = entry.getValue();

					// 좋아요 개수
					int totalCount = likedMembers.size();

					// 요청한 사용자가 목록에 있다면 true, 아니라면 false
					boolean liked = likedMembers.contains(username);

					return new PlaceBlockLikeSummary(totalCount, liked, likedMembers);
				}
			));
	}

	@Override
	public Map<Long, PlaceBlockCommentSummary> findPlaceBlockComments(List<Long> placeBlockIds) {
		QPlaceBlockComment latestComment = new QPlaceBlockComment("latestComment");

		// 각 장소 블록의 댓글 개수 조회
		Map<Long, Long> commentCountMap = queryFactory
			.select(placeBlockComment.placeBlock.id, placeBlockComment.count())
			.from(placeBlockComment)
			.where(
				placeBlockComment.deletedAt.isNull(),
				placeBlockComment.placeBlock.id.in(placeBlockIds)
			)
			.groupBy(placeBlockComment.placeBlock.id)
			.fetch()
			.stream()
			.collect(Collectors.toMap(
				tuple -> tuple.get(placeBlockComment.placeBlock.id),
				tuple -> Objects.requireNonNull(tuple.get(placeBlockComment.count()))
			));

		// 각 장소 블록의 가장 최신 댓글 ID 조회 서브 쿼리
		JPQLSubQuery<Long> latestCommentIds = JPAExpressions
			.select(latestComment.id.max())
			.from(latestComment)
			.where(
				latestComment.deletedAt.isNull(),
				latestComment.placeBlock.id.in(placeBlockIds)
			)
			.groupBy(latestComment.placeBlock.id);

		// 각 장소 블록의 마지막 댓글 조회
		Map<Long, PlaceBlockLastComment> lastCommentMap = queryFactory
			.select(
				placeBlockComment.placeBlock.id,
				new QPlaceBlockLastComment(
					placeBlockComment.content,
					placeBlockComment.content // TODO: author로 변경
				)
			)
			.from(placeBlockComment)
			.where(placeBlockComment.id.in(latestCommentIds))
			.fetch()
			.stream()
			.collect(Collectors.toMap(
				tuple -> tuple.get(placeBlockComment.placeBlock.id),
				tuple -> Objects.requireNonNull(tuple.get(1, PlaceBlockLastComment.class))
			));

		return commentCountMap.entrySet().stream()
			.collect(Collectors.toMap(
				Map.Entry::getKey,
				entry -> new PlaceBlockCommentSummary(
					entry.getValue(),
					lastCommentMap.get(entry.getKey())
				)
			));
	}
}
