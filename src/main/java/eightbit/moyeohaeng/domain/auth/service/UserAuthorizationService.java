package eightbit.moyeohaeng.domain.auth.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import eightbit.moyeohaeng.domain.auth.RequiredUserRole;
import eightbit.moyeohaeng.domain.project.entity.Project;
import eightbit.moyeohaeng.domain.project.repository.ProjectRepository;
import eightbit.moyeohaeng.domain.project.repository.TravelerRepository;
import eightbit.moyeohaeng.domain.team.repository.TeamMemberRepository;
import eightbit.moyeohaeng.global.security.CustomUserDetails;
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

	public RequiredUserRole resolveProjectRole(Long projectId, HttpServletRequest request) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Project project = projectRepository.findById(projectId)
			.orElseThrow(() -> new IllegalArgumentException("Project not found: " + projectId));

		if (auth != null && auth.getPrincipal() instanceof CustomUserDetails user) {
			// Guest via share token
			if (user.isGuest()) {
				Long pid = user.getShareProjectId();
				if (pid == null || !projectId.equals(pid))
					return RequiredUserRole.VIEWER;
				String type = user.getGuestType();
				if ("guest".equalsIgnoreCase(type))
					return RequiredUserRole.GUEST;
				return RequiredUserRole.ANY;
			}

			// Logged-in member
			Long memberId = user.getMemberId();
			// OWNER
			if (project.getCreator() != null && project.getCreator().getId() != null
				&& project.getCreator().getId().equals(memberId)) {
				return RequiredUserRole.OWNER;
			}
			// MEMBER (같은 팀)
			boolean isTeamMember = teamMemberRepository.existsByTeam_IdAndMember_Id(project.getTeam().getId(),
				memberId);
			if (isTeamMember)
				return RequiredUserRole.MEMBER;
			// TRAVELER (초대 받은 유저)
			boolean isTraveler = travelerRepository.existsByProject_IdAndMember_Id(projectId, memberId);
			if (isTraveler)
				return RequiredUserRole.TRAVELER;
			// Logged-in but no relation: VIEWER
			return RequiredUserRole.ANY;
		}

		// No authentication principal
		return RequiredUserRole.VIEWER;
	}

	public RequiredUserRole resolveTeamRole(Long teamId, HttpServletRequest request) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null && auth.getPrincipal() instanceof CustomUserDetails user) {
			Long memberId = user.getMemberId();
			boolean isTeamMember = teamMemberRepository.existsByTeam_IdAndMember_Id(teamId, memberId);
			return isTeamMember ? RequiredUserRole.MEMBER : RequiredUserRole.VIEWER;
		}
		return RequiredUserRole.VIEWER;
	}

	// JwtAuthenticationFilter sets the authenticated principal; no header parsing here anymore.

	public static boolean isAllowed(RequiredUserRole actual, RequiredUserRole required) {
		int a = rank(actual);
		int r = rank(required);
		return a >= r;
	}

	private static int rank(RequiredUserRole role) {
		return switch (role) {
			case ANY -> 0;
			case VIEWER -> 1;
			case GUEST -> 2;
			case TRAVELER -> 3;
			case MEMBER -> 4;
			case OWNER -> 5;
		};
	}
}
