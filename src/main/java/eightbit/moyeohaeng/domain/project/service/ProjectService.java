package eightbit.moyeohaeng.domain.project.service;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import eightbit.moyeohaeng.domain.auth.UserRole;
import eightbit.moyeohaeng.domain.member.entity.member.Member;
import eightbit.moyeohaeng.domain.member.service.MemberService;
import eightbit.moyeohaeng.domain.project.common.exception.ProjectErrorCode;
import eightbit.moyeohaeng.domain.project.common.exception.ProjectException;
import eightbit.moyeohaeng.domain.project.dto.ProjectDto;
import eightbit.moyeohaeng.domain.project.dto.condition.ProjectSearchCondition;
import eightbit.moyeohaeng.domain.project.dto.request.ProjectCreateRequest;
import eightbit.moyeohaeng.domain.project.dto.request.ProjectSortType;
import eightbit.moyeohaeng.domain.project.dto.request.ProjectUpdateRequest;
import eightbit.moyeohaeng.domain.project.entity.Project;
import eightbit.moyeohaeng.domain.project.repository.ProjectRepository;
import eightbit.moyeohaeng.domain.team.entity.Team;
import eightbit.moyeohaeng.domain.team.entity.TeamMember;
import eightbit.moyeohaeng.domain.team.entity.TeamRole;
import eightbit.moyeohaeng.domain.team.repository.TeamMemberRepository;
import eightbit.moyeohaeng.domain.team.repository.TeamRepository;
import eightbit.moyeohaeng.global.event.sse.SseEmitterService;
import eightbit.moyeohaeng.global.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true) // 기본 조회 최적화
public class ProjectService {

	private final ProjectRepository projectRepository;
	private final SseEmitterService sseEmitterService;
	private final ChannelTopic channelTopic;
	private final MemberService memberService;
	private final TeamRepository teamRepository;
	private final TeamMemberRepository teamMemberRepository;

	final ProjectDto testProjectDTO = ProjectDto.builder().title("테스트 project").build();

	@Transactional
	public ProjectDto create(ProjectCreateRequest request, CustomUserDetails currentUser) {
		// CustomUserDetails에서 ID를 이용하여 Member 조회
		Member member = memberService.findById(currentUser.getMemberId());
		// Create and persist a Team for the project
		String teamName =
			(request.title() == null || request.title().isBlank()) ? "Project Team" : request.title() + " Team";
		Team team = teamRepository.save(Team.builder().name(teamName).build());
		// TODO TeamMember 권한 체크 로직 추가 (추후 권한 정책 적용)

		// 프로젝트 생성 및 소유자 설정
		Project project = Project.create(team, member, request.title(), request.startDate(), request.endDate());
		Project savedProject = projectRepository.save(project);

		// Create OWNER membership for creator
		TeamMember ownerMembership = TeamMember.builder()
			.team(team)
			.member(member)
			.teamRole(TeamRole.OWNER)
			.build();
		teamMemberRepository.save(ownerMembership);

		return ProjectDto.from(savedProject);
	}

	public List<ProjectDto> find() {
		return ProjectDto.from(projectRepository.findAll());
		// return projectRepository.findAll().stream()
		// 	.map(ProjectDto::from)
		// 	.collect(Collectors.toList());

	}
	/**
	 * 현재 사용자가 접근 가능한 프로젝트 조회
	 * 프로젝트의 소유자이거나 프로젝트 팀의 멤버인 경우에만 접근이 가능
	 *
	 * @param projectId 조회할 프로젝트 ID
	 * @param currentUser 현재 로그인한 사용자 정보
	 * @return 조회된 프로젝트 DTO
	 * @throws ProjectException 프로젝트가 존재하지 않거나 접근 권한이 없는 경우
	 */
	public ProjectDto getById(Long projectId, CustomUserDetails currentUser) {
		// 프로젝트 조회 및 접근 권한 검사를 하나의 쿼리로 처리
		Project project = findMyProjectById(projectId, currentUser);
		return ProjectDto.from(project);
	}


	/**
	 * 외부 공유가 허용된 프로젝트 조회
	 * 비로그인 사용자도 접근 가능
	 *
	 * @param projectId 조회할 프로젝트 ID
	 * @return 조회된 프로젝트 DTO
	 * @throws ProjectException 프로젝트가 존재하지 않거나 외부 공유가 허용되지 않은 경우
	 */
	public ProjectDto findById(Long projectId) {
		// 프로젝트 조회 및 외부 공유 허용 여부 검사
		Project project = findEntityById(projectId);
		ensureShareAllowed(projectId);
		return ProjectDto.from(project);
	}

	// TODO RequiredUserRole 이름 변경

	/**
	 * 프로젝트가 외부 공유를 허용하는지 검사합니다. 
	 * 허용되지 않으면 예외를 던집니다.
	 * @return UserRole {GUEST, VIEWER}
	 */
	public UserRole ensureShareAllowed(Long projectId) {
		Project project = findEntityById(projectId);

		if (!(project.isAllowGuest() || project.isAllowViewer())) {
			throw new ProjectException(ProjectErrorCode.SHARE_NOT_ALLOWED);
		}

		if (project.isAllowGuest()) {
			return UserRole.GUEST;
		}
		return UserRole.VIEWER;

	}
	/**
	 * SSE 연결
	 *
	 * @param projectId 프로젝트 Id
	 * @param user 사용자 정보
	 * @return SseEmitter
	 */
	public SseEmitter connect(Long projectId, String lastEventId, String user) {
		return sseEmitterService.subscribe(channelTopic, projectId, lastEventId, user);
	}
	public List<ProjectDto> searchMyProjects(CustomUserDetails currentUser, ProjectSearchCondition condition) {
		Sort sort = getSortFromType(condition.sortType());
		List<Project> projects;

		// 내가 속한 팀에 프로젝트 조회
		if (condition.hasTeamFilter()) {
			projects = projectRepository.findByTeamId(condition.teamId(), sort);
		} else { // 내가 접근 가능한  프로젝트 조회
			Long memberId = currentUser.getMemberId();
			projects = projectRepository.findByMemberId(memberId, sort);
		}

		return ProjectDto.from(projects);
	}

	private Sort getSortFromType(ProjectSortType sortType) {
		return switch (sortType) {
			case MODIFIED_AT_DESC -> Sort.by(Sort.Direction.DESC, "modifiedAt");
			case MODIFIED_AT_ASC -> Sort.by(Sort.Direction.ASC, "modifiedAt");
			case CREATED_AT_DESC -> Sort.by(Sort.Direction.DESC, "createdAt");
			case CREATED_AT_ASC -> Sort.by(Sort.Direction.ASC, "createdAt");
			default -> Sort.by(Sort.Direction.DESC, "modifiedAt");
		};
	}

	@Transactional
	public ProjectDto update(Long projectId, ProjectUpdateRequest request, CustomUserDetails currentUser) {
		// Project project = projectRepository.findById(projectId)
		// 	.orElseThrow(() -> new ProjectException(ProjectErrorCode.PROJECT_NOT_FOUND, projectId));

		// 사용자 권한 체크 - 프로젝트 소유자인지 확인
		// if (!project.isOwnedBy(member)) {
		// 	throw new ProjectException(ProjectErrorCode.FORBIDDEN);
		// }

		return testProjectDTO;
	}

	@Transactional
	public void delete(Long projectId, CustomUserDetails currentUser) {
		// CustomUserDetails에서 ID를 이용하여 Member 조회
		// Member member = memberService.findById(currentUser.getMemberId());
		// Project project = findEntityById(projectId);
		// // 사용자 권한 체크 - 프로젝트 소유자만 삭제 가능 (추후 관리자 권한 추가 가능)
		// if (!project.isOwnedBy(member)) {
		// 	throw new ProjectException(ProjectErrorCode.FORBIDDEN);
		// }

		// projectRepository.delete(project);
	}

	public Project findEntityById(Long projectId) {
		// Project project = projectRepository.findById(projectId)
		// 	.orElseThrow(() -> new ProjectException(ProjectErrorCode.PROJECT_NOT_FOUND, projectId));
		//
		// // return project;
		return Project.builder().title("테스트 entity").build();
	}
	/**
	 * 현재 사용자의 프로젝트 조회
	 *
	 * @param projectId 조회할 프로젝트 ID
	 * @param currentUser 현재 로그인한 사용자 정보
	 * @return 조회된 프로젝트 엔티티
	 * @throws ProjectException 프로젝트가 존재하지 않거나 접근 권한이 없는 경우
	 */
	protected Project findMyProjectById(Long projectId, CustomUserDetails currentUser) {
		return projectRepository.findByIdWithAccessCheck(projectId, currentUser.getMemberId())
			.orElseThrow(() -> new ProjectException(ProjectErrorCode.PROJECT_NOT_FOUND, projectId));
	}

}
