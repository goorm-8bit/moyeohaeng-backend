package eightbit.moyeohaeng.domain.project.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import eightbit.moyeohaeng.domain.auth.UserRole;
import eightbit.moyeohaeng.domain.member.entity.member.Member;
import eightbit.moyeohaeng.domain.member.service.MemberService;
import eightbit.moyeohaeng.domain.project.common.exception.ProjectErrorCode;
import eightbit.moyeohaeng.domain.project.common.exception.ProjectException;
import eightbit.moyeohaeng.domain.project.dto.ProjectDto;
import eightbit.moyeohaeng.domain.project.dto.request.ProjectCreateRequest;
import eightbit.moyeohaeng.domain.project.dto.request.ProjectUpdateRequest;
import eightbit.moyeohaeng.domain.project.entity.Project;
import eightbit.moyeohaeng.domain.project.repository.ProjectRepository;
import eightbit.moyeohaeng.domain.team.entity.Team;
import eightbit.moyeohaeng.domain.team.entity.TeamMember;
import eightbit.moyeohaeng.domain.team.entity.TeamRole;
import eightbit.moyeohaeng.domain.team.repository.TeamMemberRepository;
import eightbit.moyeohaeng.domain.team.repository.TeamRepository;
import eightbit.moyeohaeng.global.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true) // 기본 조회 최적화
public class ProjectService {

	private final ProjectRepository projectRepository;
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
			.role(TeamRole.OWNER)
			.build();
		teamMemberRepository.save(ownerMembership);

		return ProjectDto.from(savedProject);
	}

	public List<ProjectDto> find() {

		List<ProjectDto> testDto = new ArrayList<>();

		return testDto;
		// return projectRepository.findAll().stream()
		// 	.map(ProjectDto::from)
		// 	.collect(Collectors.toList());

	}

	public ProjectDto findById(Long projectId) {
		// Project project = projectRepository.findById(projectId)
		// 	.orElseThrow(() -> new ProjectException(ProjectErrorCode.PROJECT_NOT_FOUND, projectId));
		return testProjectDTO;
	}

	// TODO RequiredUserRole 이름 변경

	/**
	 * 프로젝트가 외부 공유를 허용하는지 검사합니다. 
	 * 허용되지 않으면 예외를 던집니다.
	 * @return UserRole {GUEST, VIEWER}
	 */
	public UserRole ensureShareAllowed(Long projectId) {
		Project project = findEntityById(projectId);

		if (project.isAllowGuest() || project.isAllowViewer()) {
			throw new ProjectException(ProjectErrorCode.SHARE_NOT_ALLOWED);
		}

		if (project.isAllowGuest()) {
			return UserRole.GUEST;
		}
		return UserRole.VIEWER;

	}

	public List<ProjectDto> findWithMe(CustomUserDetails currentUser) {

		// 	// TODO 서비 내용 추가
		List<ProjectDto> testDto = new ArrayList<>();

		testDto.add(testProjectDTO);
		return testDto;
	}

	@Transactional
	public ProjectDto update(Long projectId, ProjectUpdateRequest request, CustomUserDetails currentUser) {
		// CustomUserDetails에서 ID를 이용하여 Member 조회
		Member member = memberService.findById(currentUser.getMemberId());
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

}
