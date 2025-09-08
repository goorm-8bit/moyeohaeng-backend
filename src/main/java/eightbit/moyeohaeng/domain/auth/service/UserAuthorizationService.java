package eightbit.moyeohaeng.domain.auth.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import eightbit.moyeohaeng.domain.auth.UserRole;
import eightbit.moyeohaeng.domain.project.entity.Project;
import eightbit.moyeohaeng.domain.project.repository.ProjectRepository;
import eightbit.moyeohaeng.domain.project.repository.TravelerRepository;
import eightbit.moyeohaeng.domain.team.repository.TeamMemberRepository;
import eightbit.moyeohaeng.global.security.CustomUserDetails;
import eightbit.moyeohaeng.global.security.UserType;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Controller에서 사용되지 않는 Service
 * UserAuthorizationGuardAspect에서만 사용되는 인가 비지니스 로직
 * */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserAuthorizationService {

	private final ProjectRepository projectRepository;
	private final TeamMemberRepository teamMemberRepository;
	private final TravelerRepository travelerRepository;

	public UserRole resolveProjectRole(Long projectId, HttpServletRequest request) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Project project = projectRepository.findById(projectId)
			.orElseThrow(() -> new IllegalArgumentException("Project not found: " + projectId));

		if (auth != null && auth.getPrincipal() instanceof CustomUserDetails user) {
			// Guest principal: decide role only by guestType
			if (user.isGuest()) {
				UserType userType = user.getUserType();
				if (UserType.GUEST == userType)
					return UserRole.GUEST;
				return UserRole.VIEWER;
			}

			// Logged-in member
			Long memberId = user.getMemberId();
			// OWNER
			if (project.getCreator() != null && project.getCreator().getId() != null
				&& project.getCreator().getId().equals(memberId)) {
				return UserRole.OWNER;
			}
			// MEMBER (같은 팀)
			boolean isTeamMember = teamMemberRepository.existsByTeam_IdAndMember_Id(project.getTeam().getId(),
				memberId);
			if (isTeamMember)
				return UserRole.MEMBER;
			// TRAVELER (초대 받은 유저)
			boolean isTraveler = travelerRepository.existsByProject_IdAndMember_Id(projectId, memberId);
			if (isTraveler)
				return UserRole.TRAVELER;
			// Logged-in but no relation: VIEWER
			return UserRole.ANY;
		}

		// No authentication principal
		return UserRole.VIEWER;
	}

	public UserRole resolveTeamRole(Long teamId, HttpServletRequest request) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null && auth.getPrincipal() instanceof CustomUserDetails user) {
			Long memberId = user.getMemberId();
			boolean isTeamMember = teamMemberRepository.existsByTeam_IdAndMember_Id(teamId, memberId);
			return isTeamMember ? UserRole.MEMBER : UserRole.VIEWER;
		}
		return UserRole.ANY;
	}

	public static boolean isAllowed(UserRole actual, UserRole required) {

		return actual.getPriority() >= required.getPriority();
	}

}
