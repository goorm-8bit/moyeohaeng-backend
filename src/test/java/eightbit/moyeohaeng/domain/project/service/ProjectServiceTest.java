package eightbit.moyeohaeng.domain.project.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import eightbit.moyeohaeng.domain.member.entity.member.Member;
import eightbit.moyeohaeng.domain.member.service.MemberService;
import eightbit.moyeohaeng.domain.project.common.exception.ProjectErrorCode;
import eightbit.moyeohaeng.domain.project.common.exception.ProjectException;
import eightbit.moyeohaeng.domain.project.dto.ProjectDto;
import eightbit.moyeohaeng.domain.project.dto.request.ProjectCreateRequest;
import eightbit.moyeohaeng.domain.project.dto.request.ProjectUpdateRequest;
import eightbit.moyeohaeng.domain.project.entity.Project;
import eightbit.moyeohaeng.domain.project.entity.ProjectAccess;
import eightbit.moyeohaeng.domain.project.repository.ProjectRepository;
import eightbit.moyeohaeng.domain.team.entity.Team;
import eightbit.moyeohaeng.domain.team.repository.TeamRepository;
import eightbit.moyeohaeng.global.security.CustomUserDetails;

@ExtendWith(MockitoExtension.class)
class ProjectServiceTest {

	@InjectMocks
	private ProjectService projectService;

	@Mock
	private ProjectRepository projectRepository;

	@Mock
	private TeamRepository teamRepository;

	@Mock
	private MemberService memberService;

	private Member member;
	private Team team;
	private Project project;
	private CustomUserDetails userDetails;

	@BeforeEach
	void setUp() {
		member = Member.builder()
			.id(1L)
			.email("test@test.com")
			.name("tester")
			.build();

		team = Team.builder()
			.id(1L)
			.name("testTeam")
			.build();

		project = Project.create(team, member, "testProject", LocalDate.now(), LocalDate.now().plusDays(7));

		userDetails = CustomUserDetails.from(member);
	}

	@Nested
	@DisplayName("프로젝트 생성 테스트")
	class CreateTest {

		@Test
		@DisplayName("정상적인 프로젝트 생성")
		void createProject_Success() {
			// given
			LocalDate now = LocalDate.now();
			ProjectCreateRequest request = new ProjectCreateRequest(
				1L,
				"testProject",
				"#ffffff",
				now,
				now.plusDays(7)
			);

			given(memberService.findById(anyLong())).willReturn(member);
			given(teamRepository.findById(anyLong())).willReturn(Optional.of(team));
			given(projectRepository.save(any(Project.class))).willReturn(project);

			// when
			ProjectDto result = projectService.create(request, userDetails);

			// then
			assertThat(result).isNotNull();
			assertThat(result.title()).isEqualTo("testProject");
			assertThat(result.travelDays()).isNotNull();
			assertThat(result.travelDays()).isEqualTo(8); // 시작일과 종료일 포함한 8일
			verify(projectRepository).save(any(Project.class));
		}

		@Test
		@DisplayName("존재하지 않는 팀으로 프로젝트 생성 시도")
		void createProject_TeamNotFound() {
			// given
			LocalDate now = LocalDate.now();
			ProjectCreateRequest request = new ProjectCreateRequest(
				999L,
				"testProject",
				"#ffffff",
				now,
				now.plusDays(7)
			);

			given(memberService.findById(anyLong())).willReturn(member);
			given(teamRepository.findById(anyLong())).willReturn(Optional.empty());

			// when & then
			assertThatThrownBy(() -> projectService.create(request, userDetails))
				.isInstanceOf(ProjectException.class);
		}
	}

	@Nested
	@DisplayName("프로젝트 조회 테스트")
	class GetByIdTest {

		@Test
		@DisplayName("외부 공유가 허용된 프로젝트 조회")
		void findById_Success() {
			// given
			project = Project.builder()
				.id(1L)
				.team(team)
				.creator(member)
				.title("testProject")
				.startDate(LocalDate.now())
				.endDate(LocalDate.now().plusDays(7))
				.projectAccess(ProjectAccess.PRIVATE)
				.isAllowGuest(true)
				.externalId(UUID.randomUUID().toString())
				.color("#ffffff")
				.build();

			given(projectRepository.findById(anyLong()))
				.willReturn(Optional.of(project));

			// when
			ProjectDto result = projectService.findById(1L);

			// then
			assertThat(result).isNotNull();
			assertThat(result.title()).isEqualTo("testProject");
			verify(projectRepository, times(2)).findById(anyLong());
		}

		@Test
		@DisplayName("외부 공유가 허용되지 않은 프로젝트 조회")
		void findById_ShareNotAllowed() {
			// given
			project = Project.builder()
				.id(1L)
				.team(team)
				.creator(member)
				.title("testProject")
				.startDate(LocalDate.now())
				.endDate(LocalDate.now().plusDays(7))
				.projectAccess(ProjectAccess.PRIVATE)
				.isAllowGuest(false)
				.externalId(UUID.randomUUID().toString())
				.color("#ffffff")
				.build();

			given(projectRepository.findById(anyLong()))
				.willReturn(Optional.of(project));

			// when & then
			assertThatThrownBy(() -> projectService.findById(1L))
				.isInstanceOf(ProjectException.class)
				.hasFieldOrPropertyWithValue("errorCode", ProjectErrorCode.SHARE_NOT_ALLOWED);
		}

		@Test
		@DisplayName("존재하지 않는 프로젝트 조회")
		void findById_ProjectNotFound() {
			// given
			given(projectRepository.findById(anyLong()))
				.willReturn(Optional.empty());

			// when & then
			assertThatThrownBy(() -> projectService.findById(1L))
				.isInstanceOf(ProjectException.class)
				.hasFieldOrPropertyWithValue("errorCode", ProjectErrorCode.PROJECT_NOT_FOUND);
		}

		@Test
		@DisplayName("정상적인 프로젝트 조회")
		void getById_Success() {
			// given
			given(projectRepository.findByIdWithAccessCheck(anyLong(), anyLong()))
				.willReturn(Optional.of(project));

			// when
			ProjectDto result = projectService.getById(1L, userDetails);

			// then
			assertThat(result).isNotNull();
			assertThat(result.title()).isEqualTo("testProject");
			verify(projectRepository).findByIdWithAccessCheck(anyLong(), anyLong());
		}

		@Test
		@DisplayName("존재하지 않는 프로젝트 조회")
		void getById_ProjectNotFound() {
			// given
			given(projectRepository.findByIdWithAccessCheck(anyLong(), anyLong()))
				.willReturn(Optional.empty());

			// when & then
			assertThatThrownBy(() -> projectService.getById(999L, userDetails))
				.isInstanceOf(ProjectException.class)
				.hasFieldOrPropertyWithValue("errorCode", ProjectErrorCode.PROJECT_NOT_FOUND);
		}
	}

	@Nested
	@DisplayName("프로젝트 수정 테스트")
	class UpdateTest {

		@Test
		@DisplayName("정상적인 프로젝트 수정")
		void update_Success() {
			// given
			LocalDate now = LocalDate.now();
			ProjectUpdateRequest request = new ProjectUpdateRequest(
				"Updated Title",
				"#000000",
				now,
				now.plusDays(14)
			);

			given(projectRepository.findByIdWithAccessCheck(anyLong(), anyLong()))
				.willReturn(Optional.of(project));

			// when
			ProjectDto result = projectService.update(1L, request, userDetails);

			// then
			assertThat(result).isNotNull();
			assertThat(result.title()).isEqualTo("Updated Title");
			assertThat(result.color()).isEqualTo("#000000");
			assertThat(result.travelDays()).isNotNull();
			assertThat(result.travelDays()).isEqualTo(15); // 시작일과 종료일 포함한 15일
			verify(projectRepository).findByIdWithAccessCheck(anyLong(), anyLong());
		}

		@Test
		@DisplayName("존재하지 않는 프로젝트 수정")
		void update_ProjectNotFound() {
			// given
			ProjectUpdateRequest request = new ProjectUpdateRequest(
				"Updated Title",
				"#000000",
				LocalDate.now(),
				LocalDate.now().plusDays(7)
			);

			given(projectRepository.findByIdWithAccessCheck(anyLong(), anyLong()))
				.willReturn(Optional.empty());

			// when & then
			assertThatThrownBy(() -> projectService.update(999L, request, userDetails))
				.isInstanceOf(ProjectException.class)
				.hasFieldOrPropertyWithValue("errorCode", ProjectErrorCode.PROJECT_NOT_FOUND);
		}

		@Test
		@DisplayName("권한 없는 사용자의 프로젝트 수정")
		void update_NotProjectOwner() {
			// given
			ProjectUpdateRequest request = new ProjectUpdateRequest(
				"Updated Title",
				"#000000",
				LocalDate.now(),
				LocalDate.now().plusDays(7)
			);

			Member otherMember = Member.builder()
				.id(999L)
				.email("other@example.com")
				.name("Other User")
				.build();

			project = Project.builder()
				.id(1L)
				.team(team)
				.creator(otherMember)
				.title("testProject")
				.startDate(LocalDate.now())
				.endDate(LocalDate.now().plusDays(7))
				.projectAccess(ProjectAccess.PRIVATE)
				.externalId(UUID.randomUUID().toString())
				.color("#ffffff")
				.build();

			given(projectRepository.findByIdWithAccessCheck(anyLong(), anyLong()))
				.willReturn(Optional.of(project));

			// when & then
			assertThatThrownBy(() -> projectService.update(1L, request, userDetails))
				.isInstanceOf(ProjectException.class)
				.hasFieldOrPropertyWithValue("errorCode", ProjectErrorCode.NOT_PROJECT_OWNER);
			verify(projectRepository).findByIdWithAccessCheck(anyLong(), anyLong());
		}

		@Test
		@DisplayName("존재하지 않는 프로젝트 수정 시도")
		void updateProject_NotFound() {
			// given
			ProjectUpdateRequest request = new ProjectUpdateRequest(
				"updatedProject",
				"#FF0000",
				LocalDate.now(),
				LocalDate.now().plusDays(14)
			);

			given(projectRepository.findByIdWithAccessCheck(anyLong(), anyLong()))
				.willReturn(Optional.empty());

			// when & then
			assertThatThrownBy(() -> projectService.update(999L, request, userDetails))
				.isInstanceOf(ProjectException.class)
				.hasFieldOrPropertyWithValue("errorCode", ProjectErrorCode.PROJECT_NOT_FOUND);
		}
	}

	@Nested
	@DisplayName("프로젝트 삭제 테스트")
	class DeleteTest {

		@Test
		@DisplayName("정상적인 프로젝트 삭제")
		void deleteProject_Success() {
			// given
			given(projectRepository.findByIdWithAccessCheck(anyLong(), anyLong()))
				.willReturn(Optional.of(project));

			// when
			projectService.delete(1L, userDetails);

			// then
			verify(projectRepository).delete(any(Project.class));
		}

		@Test
		@DisplayName("존재하지 않는 프로젝트 삭제 시도")
		void deleteProject_NotFound() {
			// given
			given(projectRepository.findByIdWithAccessCheck(anyLong(), anyLong()))
				.willReturn(Optional.empty());

			// when & then
			assertThatThrownBy(() -> projectService.delete(999L, userDetails))
				.isInstanceOf(ProjectException.class)
				.hasFieldOrPropertyWithValue("errorCode", ProjectErrorCode.PROJECT_NOT_FOUND);
		}
	}

	@Nested
	@DisplayName("프로젝트 외부 공유 테스트")
	class EnsureShareAllowedTest {

		@Test
		@DisplayName("외부 공유가 허용된 프로젝트")
		void ensureShareAllowed_Success() {
			// given
			project = Project.builder()
				.id(1L)
				.team(team)
				.creator(member)
				.title("testProject")
				.startDate(LocalDate.now())
				.endDate(LocalDate.now().plusDays(7))
				.projectAccess(ProjectAccess.PUBLIC)
				.externalId(UUID.randomUUID().toString())
				.color("#ffffff")
				.build();
			project.updateShareOption(true, false);
			given(projectRepository.findById(anyLong()))
				.willReturn(Optional.of(project));

			// when & then
			assertDoesNotThrow(() -> projectService.ensureShareAllowed(1L));
		}

		@Test
		@DisplayName("외부 공유가 허용되지 않은 프로젝트")
		void ensureShareAllowed_NotAllowed() {
			// given
			given(projectRepository.findById(anyLong()))
				.willReturn(Optional.of(project));

			// when & then
			assertThatThrownBy(() -> projectService.ensureShareAllowed(1L))
				.isInstanceOf(ProjectException.class)
				.hasFieldOrPropertyWithValue("errorCode", ProjectErrorCode.SHARE_NOT_ALLOWED);
		}
	}
}
