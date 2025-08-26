package eightbit.moyeohaeng.domain.project.placeblock.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import eightbit.moyeohaeng.domain.project.placeblock.dto.request.PlaceBlockCreateRequest;
import eightbit.moyeohaeng.domain.project.placeblock.dto.request.PlaceBlockUpdateRequest;
import eightbit.moyeohaeng.domain.project.placeblock.dto.response.PlaceBlockResponse;
import eightbit.moyeohaeng.domain.project.placeblock.entity.PlaceBlock;
import eightbit.moyeohaeng.domain.project.placeblock.entity.PlaceBlockType;
import eightbit.moyeohaeng.domain.project.placeblock.exception.PlaceBlockErrorCode;
import eightbit.moyeohaeng.domain.project.placeblock.exception.PlaceBlockException;
import eightbit.moyeohaeng.domain.project.placeblock.repository.PlaceBlockRepository;

@ExtendWith(MockitoExtension.class)
class PlaceBlockServiceTest {

	@InjectMocks
	private PlaceBlockService service;

	@Mock
	private PlaceBlockRepository repository;

	private final Long PROJECT_ID = 1L;
	private final Long USER_ID = 1L;
	private final String OWNER_ROLE = "OWNER";
	private final String VIEWER_ROLE = "VIEWER";
	private final String EDITOR_ROLE = "EDITOR";
	private final PlaceBlockCreateRequest CREATE_REQUEST = new PlaceBlockCreateRequest(
		"테스트 블록", null, 37.0, 127.0, null, null, null, null, null, null, null, "test@test.com",
		PlaceBlockType.PLACE_BLOCK
	);

	@DisplayName("생성_성공_유효한_요청")
	@Test
	void create_success_with_valid_request() {
		// given
		PlaceBlock placeBlock = PlaceBlock.of(PROJECT_ID, CREATE_REQUEST.name(), CREATE_REQUEST.latitude(),
			CREATE_REQUEST.longitude(), CREATE_REQUEST.author(), CREATE_REQUEST.type());
		given(repository.countByProjectId(PROJECT_ID)).willReturn(50L);
		given(repository.save(any(PlaceBlock.class))).willReturn(placeBlock);

		// when
		PlaceBlockResponse response = service.create(PROJECT_ID, USER_ID, OWNER_ROLE, CREATE_REQUEST);

		// then
		assertThat(response.name()).isEqualTo(CREATE_REQUEST.name());
		verify(repository).save(any(PlaceBlock.class));
	}

	@DisplayName("생성_실패_최대_개수_초과")
	@Test
	void create_fail_when_limit_exceeded() {
		// given
		given(repository.countByProjectId(PROJECT_ID)).willReturn(100L);

		// when & then
		assertThatThrownBy(() -> service.create(PROJECT_ID, USER_ID, OWNER_ROLE, CREATE_REQUEST))
			.isInstanceOf(PlaceBlockException.class)
			.hasMessage(PlaceBlockErrorCode.LIMIT_EXCEEDED.getMessage());

		verify(repository, never()).save(any(PlaceBlock.class));
	}

	@DisplayName("수정_성공_OWNER_권한")
	@Test
	void update_success_with_owner_role() {
		// given
		Long placeBlockId = 1L;
		PlaceBlock existingBlock = PlaceBlock.of(PROJECT_ID, "기존 블록", 37.0, 127.0, "author", PlaceBlockType.PIN);
		PlaceBlockUpdateRequest request = new PlaceBlockUpdateRequest("새로운 이름", null, null, null, null, null, null,
			null, null, null, null, null, null);

		given(repository.findByIdAndProjectId(placeBlockId, PROJECT_ID)).willReturn(Optional.of(existingBlock));

		// when
		service.update(PROJECT_ID, placeBlockId, USER_ID, OWNER_ROLE, request);

		// then
		verify(repository).save(any(PlaceBlock.class));
	}

	@DisplayName("수정_성공_EDITOR_권한")
	@Test
	void update_success_with_editor_role() {
		// given
		Long placeBlockId = 1L;
		PlaceBlock existingBlock = PlaceBlock.of(PROJECT_ID, "기존 블록", 37.0, 127.0, "author", PlaceBlockType.PIN);
		PlaceBlockUpdateRequest request = new PlaceBlockUpdateRequest("새로운 이름", null, null, null, null, null, null,
			null, null, null, null, null, null);

		given(repository.findByIdAndProjectId(placeBlockId, PROJECT_ID)).willReturn(Optional.of(existingBlock));

		// when
		service.update(PROJECT_ID, placeBlockId, USER_ID, EDITOR_ROLE, request);

		// then
		verify(repository).save(any(PlaceBlock.class));
	}

	@DisplayName("수정_실패_VIEWER_권한")
	@Test
	void update_fail_with_viewer_role() {
		// given
		Long placeBlockId = 1L;
		PlaceBlockUpdateRequest request = new PlaceBlockUpdateRequest("새 이름", null, null, null, null, null, null, null,
			null, null, null, null, null);

		// when & then
		assertThatThrownBy(() -> service.update(PROJECT_ID, placeBlockId, USER_ID, VIEWER_ROLE, request))
			.isInstanceOf(PlaceBlockException.class)
			.hasMessage(PlaceBlockErrorCode.FORBIDDEN.getMessage());
	}

	@DisplayName("삭제_성공")
	@Test
	void delete_success() {
		// given
		Long placeBlockId = 1L;
		PlaceBlock placeBlock = PlaceBlock.of(PROJECT_ID, "테스트", 37.0, 127.0, "test", PlaceBlockType.PIN);
		given(repository.findByIdAndProjectId(placeBlockId, PROJECT_ID)).willReturn(Optional.of(placeBlock));

		// when
		service.delete(PROJECT_ID, placeBlockId, USER_ID, OWNER_ROLE);

		// then
		verify(repository).delete(placeBlock);
	}

	@DisplayName("삭제_실패_권한_없음")
	@Test
	void delete_fail_with_forbidden_role() {
		// given
		Long placeBlockId = 1L;

		// when & then
		assertThatThrownBy(() -> service.delete(PROJECT_ID, placeBlockId, USER_ID, VIEWER_ROLE))
			.isInstanceOf(PlaceBlockException.class)
			.hasMessage(PlaceBlockErrorCode.FORBIDDEN.getMessage());
	}

	@DisplayName("조회_성공")
	@Test
	void get_success() {
		// given
		Long placeBlockId = 1L;
		PlaceBlock placeBlock = PlaceBlock.of(PROJECT_ID, "테스트", 37.0, 127.0, "test", PlaceBlockType.PIN);
		given(repository.findByIdAndProjectId(placeBlockId, PROJECT_ID)).willReturn(Optional.of(placeBlock));

		// when
		PlaceBlockResponse response = service.get(PROJECT_ID, placeBlockId);

		// then
		assertThat(response.id()).isEqualTo(placeBlock.getId());
		assertThat(response.name()).isEqualTo(placeBlock.getName());
	}

	@DisplayName("조회_실패_장소_블록_없음")
	@Test
	void get_fail_with_not_found() {
		// given
		Long placeBlockId = 1L;
		given(repository.findByIdAndProjectId(placeBlockId, PROJECT_ID)).willReturn(Optional.empty());

		// when & then
		assertThatThrownBy(() -> service.get(PROJECT_ID, placeBlockId))
			.isInstanceOf(PlaceBlockException.class)
			.hasMessage(PlaceBlockErrorCode.PLACE_BLOCK_NOT_FOUND.getMessage());
	}
}