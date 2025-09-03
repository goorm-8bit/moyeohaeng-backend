package eightbit.moyeohaeng.domain.itinerary.repository;

import java.time.LocalTime;

import org.springframework.stereotype.Repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import eightbit.moyeohaeng.domain.itinerary.entity.QTimeBlock;
import eightbit.moyeohaeng.domain.project.entity.QProject;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class TimeBlockRepositoryImpl implements TimeBlockRepositoryCustom {

	private final JPAQueryFactory queryFactory;

	@Override
	public boolean existsOverlappingTimeBlock(Long projectId, Integer day, LocalTime startTime, LocalTime endTime) {
		// 시간 미정인 경우 겹침 없음
		if (startTime == null && endTime == null) {
			return false;
		}

		QProject project = QProject.project;
		QTimeBlock timeBlock = QTimeBlock.timeBlock;

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

		return queryFactory
			.selectOne()
			.from(timeBlock)
			.join(timeBlock.project, project)
			.where(
				project.id.eq(projectId),
				timeBlock.day.eq(day),
				overlap
			)
			.fetchFirst() != null;
	}
}
