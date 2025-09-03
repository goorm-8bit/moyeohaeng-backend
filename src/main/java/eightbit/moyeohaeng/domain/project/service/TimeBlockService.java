package eightbit.moyeohaeng.domain.project.service;

import java.time.LocalTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import eightbit.moyeohaeng.domain.project.common.exception.PlaceErrorCode;
import eightbit.moyeohaeng.domain.project.common.exception.PlaceException;
import eightbit.moyeohaeng.domain.project.common.exception.ProjectErrorCode;
import eightbit.moyeohaeng.domain.project.common.exception.ProjectException;
import eightbit.moyeohaeng.domain.project.common.exception.TimeBlockErrorCode;
import eightbit.moyeohaeng.domain.project.common.exception.TimeBlockException;
import eightbit.moyeohaeng.domain.project.dto.request.TimeBlockCreateRequest;
import eightbit.moyeohaeng.domain.project.dto.response.TimeBlockResponse;
import eightbit.moyeohaeng.domain.project.entity.Place;
import eightbit.moyeohaeng.domain.project.entity.Project;
import eightbit.moyeohaeng.domain.project.entity.TimeBlock;
import eightbit.moyeohaeng.domain.project.repository.PlaceRepository;
import eightbit.moyeohaeng.domain.project.repository.ProjectRepository;
import eightbit.moyeohaeng.domain.project.repository.TimeBlockRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TimeBlockService {

	private final TimeBlockRepository timeBlockRepository;
	private final ProjectRepository projectRepository;
	private final PlaceRepository placeRepository;

	@Transactional
	public TimeBlockResponse create(Long projectId, TimeBlockCreateRequest request) {
		validateTimeBlockExists(projectId, request.day(), request.startTime(), request.endTime());

		// TODO: day에 대한 검증 추가

		Project project = projectRepository.findById(projectId)
			.orElseThrow(() -> new ProjectException(ProjectErrorCode.PROJECT_NOT_FOUND));

		Place place = placeRepository.findById(request.placeId())
			.orElseThrow(() -> new PlaceException(PlaceErrorCode.PLACE_NOT_FOUND));

		TimeBlock timeBlock = TimeBlock.of(
			request.day(),
			request.startTime(),
			request.endTime(),
			request.memo(),
			project,
			place
		);

		timeBlockRepository.save(timeBlock);
		return TimeBlockResponse.from(timeBlock);
	}

	private void validateTimeBlockExists(Long projectId, Integer day, LocalTime startTime, LocalTime endTime) {
		// 다른 시간 블록과 겹치는 시간이 있는지 확인
		boolean overlap = timeBlockRepository.existsOverlappingTimeBlock(projectId, day, startTime, endTime);
		if (overlap) {
			throw new TimeBlockException(TimeBlockErrorCode.TIME_BLOCK_CONFLICT);
		}
	}
}
