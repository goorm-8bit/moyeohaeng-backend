package eightbit.moyeohaeng.domain.project.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
import eightbit.moyeohaeng.global.domain.auth.ShareTokenProvider;
import eightbit.moyeohaeng.global.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true) // 기본 조회 최적화
public class ProjectService {

	private final ProjectRepository projectRepository;
	private final MemberService memberService;
	private final ShareTokenProvider shareTokenProvider;
	// TODO TeamRepository 추가

	final ProjectDto testProjectDTO = ProjectDto.builder().title("테스트 project").build();

	@Transactional
	public ProjectDto create(ProjectCreateRequest request, CustomUserDetails currentUser) {
		// CustomUserDetails에서 ID를 이용하여 Member 조회
		Member member = memberService.findById(currentUser.getMemberId());
		Team team = Team.builder().build();    // TODO TeamRepository 추가
		// TODO TeamMember 권한 체크 로직 추가

		// 프로젝트 생성 및 소유자 설정
		Project project = Project.create(team, member, request.title(), request.startDate(), request.endDate());
		// Project savedProject = projectRepository.save(project);  //TODO TeamRepository 추가 주석 해제

		return testProjectDTO;
	}

	public List<ProjectDto> find() {

		List<ProjectDto> testDto = new ArrayList<>();

		testDto.add(testProjectDTO);

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

	/**
	 * Owner가 프로젝트 공유 토큰(guest/viewer)을 발급합니다.
	 * days가 null이면 기본 3일.
	 */
	public String createShareToken(Long projectId, String userType, Integer days, CustomUserDetails currentUser) {
		// Owner 확인
		Member owner = memberService.findById(currentUser.getMemberId());
		Project project = findEntityById(projectId);
		if (!project.isOwnedBy(owner)) {
			throw new ProjectException(ProjectErrorCode.CREATE_SHARE_TOKEN_FAIL);
		}
		int expireDays = days == null ? 3 : Math.min(Math.max(days, 1), 14); // 1~14일 제한
		return shareTokenProvider.createShareToken(projectId, owner.getEmail(), userType, expireDays);
	}

	public Project findEntityById(Long projectId) {
		// Project project = projectRepository.findById(projectId)
		// 	.orElseThrow(() -> new ProjectException(ProjectErrorCode.PROJECT_NOT_FOUND, projectId));
		//
		// // return project;
		return Project.builder().title("테스트 entity").build();
	}

}
