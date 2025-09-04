package eightbit.moyeohaeng.domain.selection.repository;

import static com.querydsl.core.group.GroupBy.*;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;

import eightbit.moyeohaeng.domain.selection.dto.response.PlaceGroupResponse;
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
			.where(placeGroup.project.id.eq(projectId))
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
}
