package eightbit.moyeohaeng.domain.itinerary.repository;

import java.time.LocalTime;

import eightbit.moyeohaeng.domain.itinerary.entity.TimeBlock;

public interface TimeBlockRepositoryCustom {

	/**
	 * 주어진 시간과 겹치는 {@link TimeBlock}이 존재하는지 확인한다.
	 *
	 * @param projectId 프로젝트 ID
	 * @param day       일차
	 * @param startTime 시작 시간
	 * @param endTime   종료 시간
	 * @return 겹치는 블록이 존재하면 {@code true}, 없으면 {@code false}
	 */
	boolean existsOverlappingTimeBlock(Long projectId, Integer day, LocalTime startTime, LocalTime endTime);
}
