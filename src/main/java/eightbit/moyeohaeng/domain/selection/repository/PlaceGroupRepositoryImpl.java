package eightbit.moyeohaeng.domain.selection.repository;

import static com.querydsl.core.group.GroupBy.*;
import static eightbit.moyeohaeng.domain.place.entity.QPlace.*;
import static eightbit.moyeohaeng.domain.selection.entity.QPlaceBlock.*;
import static eightbit.moyeohaeng.domain.selection.entity.QPlaceGroupBlock.*;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;

import eightbit.moyeohaeng.domain.selection.dto.response.PlaceBlockResponse;
import eightbit.moyeohaeng.domain.selection.dto.response.PlaceGroupResponse;
import eightbit.moyeohaeng.domain.selection.dto.response.QPlaceBlockResponse;
import eightbit.moyeohaeng.domain.selection.dto.response.QPlaceGroupResponse;
import eightbit.moyeohaeng.domain.selection.entity.QPlaceGroup;
import eightbit.moyeohaeng.domain.selection.entity.QPlaceGroupBlock;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class PlaceGroupRepositoryImpl implements PlaceGroupRepositoryCustom {

	private final JPAQueryFactory queryFactory;

	@Override
	public List<PlaceGroupResponse> findPlaceGroups(Long projectId) {
		QPlaceGroup placeGroup = QPlaceGroup.placeGroup;
		QPlaceGroupBlock placeGroupBlock = QPlaceGroupBlock.placeGroupBlock;

		return queryFactory
			.from(placeGroup)
			.leftJoin(placeGroupBlock).on(placeGroupBlock.placeGroup.id.eq(placeGroup.id))
			.where(
				placeGroup.deletedAt.isNull(),
				placeGroup.project.id.eq(projectId)
			)
			.orderBy(placeGroup.createdAt.desc())
			.transform(groupBy(placeGroup.id).list(
				new QPlaceGroupResponse(
					placeGroup.id,
					placeGroup.name,
					placeGroup.color,
					placeGroup.memo,
					list(placeGroupBlock.placeBlock.id)
				)
			));
	}

	@Override
	public List<PlaceBlockResponse> findPlaceBlocksByGroupId(Long projectId, Long placeGroupId) {
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
			.from(placeGroupBlock)
			.join(placeGroupBlock.placeBlock, placeBlock)
			.join(placeBlock.place, place)
			.where(
				placeBlock.deletedAt.isNull(),
				placeBlock.project.id.eq(projectId),
				placeGroupBlock.placeGroup.id.eq(placeGroupId)
			)
			.orderBy(placeBlock.createdAt.desc())
			.fetch();
	}
}
