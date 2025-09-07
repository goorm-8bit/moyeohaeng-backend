package eightbit.moyeohaeng.domain.itinerary.repository;

import java.time.LocalTime;
import java.util.List;

import eightbit.moyeohaeng.domain.itinerary.dto.response.TimeBlockResponse;
import eightbit.moyeohaeng.domain.itinerary.entity.TimeBlock;

public interface TimeBlockRepositoryCustom {

	/**
	 * 주어진 시간과 겹치는 {@link TimeBlock}이 존재하는지 확인한다.<br>
	 * timeBlockId가 null이 아니라면 해당 시간 블록을 제외하는 조건을 추가한다.
	 *
	 * @param projectId 프로젝트 ID
	 * @param excludeId 제외할 시간 블록 ID
	 * @param day       일차
	 * @param startTime 시작 시간
	 * @param endTime   종료 시간
	 * @return 겹치는 블록이 존재하면 {@code true}, 없으면 {@code false}
	 */
	boolean existsOverlappingTimeBlock(Long projectId, Long excludeId, Integer day, LocalTime startTime,
		LocalTime endTime);

	/**
	 * 시간 블록 목록을 조회한다.
	 *
	 * @param projectId 프로젝트 ID
	 * @param day 일차
	 * @return 시간 블록 목록
	 */
	List<TimeBlockResponse> findTimeBlocks(Long projectId, Integer day);
}
