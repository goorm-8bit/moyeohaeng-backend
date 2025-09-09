package eightbit.moyeohaeng.domain.project.service;

import java.util.Collections;
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
import eightbit.moyeohaeng.domain.project.dto.response.PresenceResponse;
import eightbit.moyeohaeng.domain.project.entity.Project;
import eightbit.moyeohaeng.domain.project.repository.ProjectRepository;
import eightbit.moyeohaeng.domain.team.entity.Team;
import eightbit.moyeohaeng.domain.team.repository.TeamMemberRepository;
import eightbit.moyeohaeng.domain.team.repository.TeamRepository;
import eightbit.moyeohaeng.global.dto.UserInfo;
import eightbit.moyeohaeng.global.event.sse.SseEmitterService;
import eightbit.moyeohaeng.global.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true) // 기본 조회 최적화
public class ProjectService {

	private final MemberService memberService;
	private final SseEmitterService sseEmitterService;
	private final PresenceService presenceService;

	private final ProjectRepository projectRepository;
	private final TeamRepository teamRepository;
	private final TeamMemberRepository teamMemberRepository;

	private final ChannelTopic channelTopic;

	/**
	 * 새로운 프로젝트 생성
	 *
	 * @param request 프로젝트 생성 요청 데이터
	 * @param currentUser 현재 로그인한 사용자 정보
	 * @return 생성된 프로젝트 DTO
	 * @throws ProjectException 팀이 존재하지 않는 경우
	 */
	@Transactional
	public ProjectDto create(ProjectCreateRequest request, CustomUserDetails currentUser) {
		Member member = memberService.findById(currentUser.getId());
		Team team = teamRepository.findById(request.teamId())
			.orElseThrow(() -> new ProjectException(ProjectErrorCode.TEAM_NOT_FOUND));

		// 프로젝트 생성 및 소유자 설정
		Project project = Project.create(team, member, request.title(), request.startDate(), request.endDate());
		Project savedProject = projectRepository.save(project);

		return ProjectDto.from(savedProject);
	}

	/**
	 * SSE 연결
	 *
	 * @param projectId 프로젝트 Id
	 * @param lastEventId 클라이언트가 마지막으로 수신한 이벤트 Id
	 * @param userInfo 사용자 정보
	 * @return SseEmitter
	 */
	public SseEmitter connect(Long projectId, String lastEventId, UserInfo userInfo) {
		return sseEmitterService.subscribe(channelTopic, projectId, lastEventId, userInfo);
	}

	public List<PresenceResponse> getConnectedMembers(Long projectId) {
		return presenceService.getConnectedMembers(projectId);
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

	public List<ProjectDto> searchMyProjects(CustomUserDetails currentUser, ProjectSearchCondition condition) {
		Sort sort = getSortFromType(condition.sortType());
		List<Project> projects;
		Long memberId = currentUser.getId();

		// 내가 속한 팀에 프로젝트 조회
		if (condition.hasTeamFilter()) {
			// 팀 멤버십 확인 (활성 멤버만)
			boolean isMember = teamMemberRepository.existsByTeam_IdAndMember_IdAndDeletedAtIsNull(condition.teamId(),
				memberId);

			if (isMember) {
				projects = projectRepository.findActiveByTeamId(condition.teamId(), sort);
			} else {
				projects = Collections.emptyList();
			}
		} else {
			// 내가 접근 가능한 모든 프로젝트 조회
			projects = projectRepository.findByMemberId(memberId, sort);
		}

		return ProjectDto.from(projects);
	}

	private Sort getSortFromType(ProjectSortType sortType) {
		ProjectSortType type = (sortType != null) ? sortType : ProjectSortType.MODIFIED_AT_DESC;

		return switch (type) {
			case MODIFIED_AT_DESC -> Sort.by(Sort.Direction.DESC, "modifiedAt");
			case MODIFIED_AT_ASC -> Sort.by(Sort.Direction.ASC, "modifiedAt");
			case CREATED_AT_DESC -> Sort.by(Sort.Direction.DESC, "createdAt");
			case CREATED_AT_ASC -> Sort.by(Sort.Direction.ASC, "createdAt");
			default -> Sort.by(Sort.Direction.DESC, "modifiedAt");
		};
	}

	/**
	 * 프로젝트 정보 업데이트
	 * 프로젝트의 소유자이거나 프로젝트 팀의 멤버만 업데이트가 가능.
	 *
	 * @param projectId 업데이트할 프로젝트 ID
	 * @param request 업데이트 요청 데이터
	 * @param currentUser 현재 로그인한 사용자 정보
	 * @return 업데이트된 프로젝트 DTO
	 * @throws ProjectException 프로젝트가 존재하지 않거나 접근 권한이 없는 경우
	 */
	@Transactional
	public ProjectDto update(Long projectId, ProjectUpdateRequest request, CustomUserDetails currentUser) {
		// findById로 프로젝트 조회 및 접근 권한 검사 (접근 권한 없으면 예외 발생)
		Project project = findMyProjectById(projectId, currentUser);
		if (!project.getCreator().getId().equals(currentUser.getId())) {
			throw new ProjectException(ProjectErrorCode.NOT_PROJECT_OWNER);
		}
		project.update(request.title(), request.color(), request.startDate(), request.endDate());
		return ProjectDto.from(project);
	}

	/**
	 * 프로젝트를 삭제
	 * 프로젝트의 소유자이거나 프로젝트 팀의 멤버만 삭제가 가능
	 *
	 * @param projectId 삭제할 프로젝트 ID
	 * @param currentUser 현재 로그인한 사용자 정보
	 * @throws ProjectException 프로젝트가 존재하지 않거나 접근 권한이 없는 경우
	 */
	@Transactional
	public void delete(Long projectId, CustomUserDetails currentUser) {
		// findById로 프로젝트 조회 및 접근 권한 검사 (접근 권한 없으면 예외 발생)
		Project project = findMyProjectById(projectId, currentUser);

		// 프로젝트 삭제
		projectRepository.delete(project);
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
		return projectRepository.findByIdWithAccessCheck(projectId, currentUser.getId())
			.orElseThrow(() -> new ProjectException(ProjectErrorCode.PROJECT_NOT_FOUND, projectId));
	}

	/**
	 * 프로젝트 엔티티 조회
	 * 이 메서드는 내부적으로 사용되며 접근 권한 검사를 포함하지 않습니다.
	 *
	 * @param projectId 조회할 프로젝트 ID
	 * @return 조회된 프로젝트 엔티티
	 * @throws ProjectException 프로젝트가 존재하지 않는 경우
	 */
	private Project findEntityById(Long projectId) {
		return projectRepository.findById(projectId)
			.orElseThrow(() -> new ProjectException(ProjectErrorCode.PROJECT_NOT_FOUND, projectId));
	}
}
