package eightbit.moyeohaeng.domain.project.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import eightbit.moyeohaeng.domain.project.dto.request.TimeBlockCreateRequest;
import eightbit.moyeohaeng.domain.project.dto.response.TimeBlockResponse;
import eightbit.moyeohaeng.domain.project.entity.PlaceBlock;
import eightbit.moyeohaeng.domain.project.entity.TimeBlock;
import eightbit.moyeohaeng.domain.project.exception.PlaceBlockErrorCode;
import eightbit.moyeohaeng.domain.project.exception.PlaceBlockException;
import eightbit.moyeohaeng.domain.project.repository.PlaceBlockRepository;
import eightbit.moyeohaeng.domain.project.repository.TimeBlockRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TimeBlockService {

	private final TimeBlockRepository timeBlockRepository;
	private final PlaceBlockRepository placeBlockRepository;

	@Transactional
	public TimeBlockResponse create(TimeBlockCreateRequest request) {
		PlaceBlock placeBlock = placeBlockRepository.findById(request.placeBlockId())
			.orElseThrow(() -> new PlaceBlockException(PlaceBlockErrorCode.PLACE_BLOCK_NOT_FOUND));

		// TODO: 다른 블록과 시간 겹치는지 확인

		TimeBlock timeBlock = TimeBlock.of(
			request.day(),
			request.startTime(),
			request.endTime(),
			request.memo(),
			placeBlock
		);

		timeBlockRepository.save(timeBlock);
		return TimeBlockResponse.from(timeBlock);
	}
}
