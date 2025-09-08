package eightbit.moyeohaeng.domain.itinerary.repository;

import static eightbit.moyeohaeng.domain.itinerary.entity.QTimeBlock.*;
import static eightbit.moyeohaeng.domain.place.entity.QPlace.*;
import static eightbit.moyeohaeng.domain.project.entity.QProject.*;

import java.time.LocalTime;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import eightbit.moyeohaeng.domain.itinerary.dto.response.QTimeBlockResponse;
import eightbit.moyeohaeng.domain.itinerary.dto.response.TimeBlockResponse;
import eightbit.moyeohaeng.domain.place.dto.response.QPlaceDetail;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class TimeBlockRepositoryImpl implements TimeBlockRepositoryCustom {

	private final JPAQueryFactory queryFactory;

	@Override
	public boolean existsOverlappingTimeBlock(Long projectId, Long excludeId, Integer day, LocalTime startTime,
		LocalTime endTime) {

		// 시간 미정인 경우 겹침 없음
		if (startTime == null && endTime == null) {
			return false;
		}

		// 둘 중 하나가 null인 경우 시작 시간 = 종료 시간으로 생각하기
		LocalTime st = startTime != null ? startTime : endTime;
		LocalTime et = endTime != null ? endTime : startTime;

		BooleanBuilder overlap = new BooleanBuilder();

		// 시간 미정인 블록은 제외
		overlap.and(timeBlock.startTime.isNotNull().or(timeBlock.endTime.isNotNull()));

		// 일정 시작 시간이 있으면 일정 시작 시간 < 종료 시간
		BooleanExpression startLt = timeBlock.startTime.isNotNull()
			.and(timeBlock.startTime.lt(et))
			.or(
				// 일정 시작 시간이 없고 종료 시간이 있으면
				timeBlock.startTime.isNull()
					// 일정 종료 시간 < 종료 시간
					.and(timeBlock.endTime.isNotNull())
					.and(timeBlock.endTime.lt(et))
			);

		// 일정 종료 시간이 있으면 일정 종료 시간 > 시작 시간
		BooleanExpression endGt = timeBlock.endTime.isNotNull()
			.and(timeBlock.endTime.gt(st))
			.or(
				// 일정 종료 시간이 없고 시작 시간이 있으면
				timeBlock.endTime.isNull()
					// 일정 시작 시간 > 시작 시간
					.and(timeBlock.startTime.isNotNull())
					.and(timeBlock.startTime.gt(st))
			);

		overlap.and(startLt).and(endGt);

		BooleanBuilder builder = new BooleanBuilder()
			.and(project.id.eq(projectId))
			.and(timeBlock.day.eq(day))
			.and(overlap);

		// 제외할 시간 블록이 있다면 조건에 추가
		if (excludeId != null) {
			builder.and(timeBlock.id.ne(excludeId));
		}

		return queryFactory
			.selectOne()
			.from(timeBlock)
			.join(timeBlock.project, project)
			.where(builder)
			.fetchFirst() != null;
	}

	@Override
	public List<TimeBlockResponse> findTimeBlocks(Long projectId, Integer day) {
		return queryFactory
			.select(new QTimeBlockResponse(
				timeBlock.id,
				timeBlock.day,
				timeBlock.startTime,
				timeBlock.endTime,
				timeBlock.memo,
				new QPlaceDetail(
					place.id,
					place.name,
					place.address,
					place.latitude,
					place.longitude,
					place.detailLink,
					place.category
				)
			))
			.from(timeBlock)
			.join(timeBlock.place, place)
			.where(
				timeBlock.deletedAt.isNull(),
				timeBlock.project.id.eq(projectId),
				eqDay(day)
			)
			.orderBy(
				// 일차 순서대로 정렬
				timeBlock.day.asc(),

				// 시작 시작이 null이면 종료 시간 기준으로 오름차순 정렬
				timeBlock.startTime.coalesce(timeBlock.endTime).asc().nullsLast()
			)
			.fetch();
	}

	private BooleanExpression eqDay(Integer day) {
		if (day == null) {
			return null;
		}
		return timeBlock.day.eq(day);
	}
}
