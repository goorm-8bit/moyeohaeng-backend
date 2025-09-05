package eightbit.moyeohaeng.domain.itinerary.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.time.LocalTime;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import eightbit.moyeohaeng.domain.itinerary.common.exception.TimeBlockErrorCode;
import eightbit.moyeohaeng.domain.itinerary.common.exception.TimeBlockException;
import eightbit.moyeohaeng.domain.itinerary.dto.request.TimeBlockCreateRequest;
import eightbit.moyeohaeng.domain.itinerary.dto.request.TimeBlockUpdateRequest;
import eightbit.moyeohaeng.domain.itinerary.dto.response.TimeBlockResponse;
import eightbit.moyeohaeng.domain.itinerary.entity.TimeBlock;
import eightbit.moyeohaeng.domain.itinerary.repository.TimeBlockRepository;
import eightbit.moyeohaeng.domain.place.entity.Place;
import eightbit.moyeohaeng.domain.place.repository.PlaceRepository;
import eightbit.moyeohaeng.domain.project.entity.Project;
import eightbit.moyeohaeng.domain.project.repository.ProjectRepository;

@ExtendWith(MockitoExtension.class)
class TimeBlockServiceTest {

	@InjectMocks
	private TimeBlockService timeBlockService;

	@Mock
	private TimeBlockRepository timeBlockRepository;
	@Mock
	private ProjectRepository projectRepository;
	@Mock
	private PlaceRepository placeRepository;

	@DisplayName("시간 블록 생성 테스트 - 성공")
	@Test
	void create_success() {
		// given
		Long projectId = 1L;
		TimeBlockCreateRequest request = new TimeBlockCreateRequest(
			1,
			LocalTime.of(20, 30),
			LocalTime.of(22, 0),
			"야경이 아름다운 곳",
			1L
		);

		given(projectRepository.findById(projectId)).willReturn(Optional.of(mock(Project.class)));
		given(placeRepository.findById(request.placeId())).willReturn(Optional.of(mock(Place.class)));

		// when
		TimeBlockResponse response = timeBlockService.create(projectId, request);

		// then
		assertThat(response.day()).isEqualTo(1);
		assertThat(response.startTime()).isEqualTo(LocalTime.of(20, 30));
		assertThat(response.endTime()).isEqualTo(LocalTime.of(22, 0));
		assertThat(response.memo()).isEqualTo("야경이 아름다운 곳");
	}

	@DisplayName("시간 블록 생성 테스트 - 실패 (시간 겹침)")
	@Test
	void create_fail_overlapTime() {
		// given
		Long projectId = 1L;
		TimeBlockCreateRequest request = new TimeBlockCreateRequest(
			1,
			LocalTime.of(20, 30),
			LocalTime.of(22, 0),
			"야경이 아름다운 곳",
			1L
		);

		given(timeBlockRepository.existsOverlappingTimeBlock(any(), eq(null), any(), any(), any()))
			.willReturn(true);

		// when & then
		assertThatThrownBy(() -> timeBlockService.create(projectId, request))
			.isInstanceOf(TimeBlockException.class)
			.hasMessage(TimeBlockErrorCode.TIME_BLOCK_CONFLICT.getMessage());
	}

	@DisplayName("시간 블록 수정 테스트 - 성공")
	@Test
	void update_success() {
		// given
		Long projectId = 1L;
		Long timeBlockId = 1L;
		TimeBlockUpdateRequest request = new TimeBlockUpdateRequest(
			2,
			LocalTime.of(12, 0),
			LocalTime.of(13, 0),
			"점심이 맛있는 곳"
		);

		given(timeBlockRepository.findByIdAndProjectId(timeBlockId, projectId)).willReturn(Optional.of(TimeBlock.of(
			1,
			LocalTime.of(20, 30),
			LocalTime.of(22, 0),
			"야경이 아름다운 곳",
			mock(Project.class),
			mock(Place.class)
		)));

		// when
		TimeBlockResponse response = timeBlockService.update(projectId, timeBlockId, request);

		// then
		assertThat(response.day()).isEqualTo(request.day());
		assertThat(response.startTime()).isEqualTo(request.startTime());
		assertThat(response.endTime()).isEqualTo(request.endTime());
		assertThat(response.memo()).isEqualTo(request.memo());
	}

	@DisplayName("시간 블록 수정 테스트 - 실패 (시작 시간 > 종료 시간)")
	@Test
	void update_fail_validateTimeRange() {
		// given
		Long projectId = 1L;
		Long timeBlockId = 1L;
		TimeBlockUpdateRequest request = new TimeBlockUpdateRequest(
			2,
			LocalTime.of(12, 0),
			LocalTime.of(10, 0),
			"점심이 맛있는 곳"
		);

		given(timeBlockRepository.findByIdAndProjectId(timeBlockId, projectId)).willReturn(Optional.of(TimeBlock.of(
			1,
			LocalTime.of(20, 30),
			LocalTime.of(22, 0),
			"야경이 아름다운 곳",
			mock(Project.class),
			mock(Place.class)
		)));

		// when & then
		assertThatThrownBy(() -> timeBlockService.update(projectId, timeBlockId, request))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage("종료 시간은 시작 시간보다 이후여야 합니다.");
	}
}
