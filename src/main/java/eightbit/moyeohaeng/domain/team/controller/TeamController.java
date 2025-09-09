package eightbit.moyeohaeng.domain.team.controller;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import eightbit.moyeohaeng.domain.team.dto.TeamDto;
import eightbit.moyeohaeng.domain.team.dto.request.CreateTeamRequestDto;
import eightbit.moyeohaeng.domain.team.dto.request.InviteMemberRequestDto;
import eightbit.moyeohaeng.domain.team.dto.request.UpdateMemberRoleRequestDto;
import eightbit.moyeohaeng.domain.team.dto.request.UpdateTeamSettingsRequestDto;
import eightbit.moyeohaeng.domain.team.dto.response.CreateTeamResponseDto;
import eightbit.moyeohaeng.domain.team.dto.response.DeleteTeamResponseDto;
import eightbit.moyeohaeng.domain.team.dto.response.GetMyTeamsResponseDto;
import eightbit.moyeohaeng.domain.team.dto.response.InviteMemberResponseDto;
import eightbit.moyeohaeng.domain.team.dto.response.RemoveMemberResponseDto;
import eightbit.moyeohaeng.domain.team.dto.response.TeamMembersResponseDto;
import eightbit.moyeohaeng.domain.team.dto.response.UpdateMemberRoleResponseDto;
import eightbit.moyeohaeng.domain.team.dto.response.UpdateTeamSettingsResponseDto;
import eightbit.moyeohaeng.domain.team.service.TeamMemberService;
import eightbit.moyeohaeng.domain.team.service.TeamService;
import eightbit.moyeohaeng.global.security.CustomUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/teams")
@Slf4j
public class TeamController implements TeamApi {

	private final TeamService teamService;
	private final TeamMemberService teamMemberService;

	@Override
	@PostMapping("/members/invite/send")
	public ResponseEntity<InviteMemberResponseDto> inviteMember(
		@AuthenticationPrincipal CustomUserDetails user,
		@RequestBody @Valid InviteMemberRequestDto requestDto
	) {
		InviteMemberResponseDto inviteMemberResponseDto = teamService.inviteMember(requestDto.teamId(), user.getId(),
			requestDto.memberId());

		return null;
	}

	@Override
	@PostMapping
	public ResponseEntity<CreateTeamResponseDto> createTeam(
		@AuthenticationPrincipal CustomUserDetails user,
		@RequestBody @Valid CreateTeamRequestDto requestDto
	) {

		TeamDto teamDto = teamService.createTeam(requestDto.newTeamName(), user.getId());

		CreateTeamResponseDto responseDto = CreateTeamResponseDto.from(teamDto);

		// 3) Location 헤더 (현재 요청 URL 뒤에 /{id})
		URI location = ServletUriComponentsBuilder
			.fromCurrentRequest()
			.path("/{teamId}")
			.buildAndExpand(Map.of("teamId", teamDto.teamId()))
			.toUri();

		return ResponseEntity.created(location).body(responseDto);
	}

	@Override
	@DeleteMapping("/{teamId}/members/{memberId}")
	public ResponseEntity<RemoveMemberResponseDto> removeMember(
		@AuthenticationPrincipal CustomUserDetails user,
		@PathVariable("memberId") Long memberId
	) {

		if (Objects.equals(user.getId(), memberId)) {

		}

		// delete member in team by memberId need role = owner

		return null;
	}

	@Override
	@PutMapping("/{teamId}/members/{memberId}")
	public ResponseEntity<UpdateMemberRoleResponseDto> updateMemberRole(
		@AuthenticationPrincipal CustomUserDetails user,
		@RequestBody UpdateMemberRoleRequestDto requestDto,
		@PathVariable("memberId") Long memberId
	) {

		// memberId 는 팀 멤버 권한을 재설정 하는 사람 (ex : 관리자)
		// 재설정을 받는 memberId 는 requestDto 에
		if (Objects.equals(user.getId(), memberId)) {

		}

		return null;
	}

	@PutMapping("/{teamId}")
	public ResponseEntity<UpdateTeamSettingsResponseDto> updateTeamSettings(
		@AuthenticationPrincipal CustomUserDetails user,
		@RequestBody UpdateTeamSettingsRequestDto requestDto,
		@PathVariable("teamId") Long teamId
	) {

		// Put team find by teamId update data in body

		return null;
	}

	@DeleteMapping("/{teamId}")
	public ResponseEntity<DeleteTeamResponseDto> deleteTeam(
		@AuthenticationPrincipal CustomUserDetails user,
		@PathVariable Long teamId
	) {

		user.getId();

		// delete team by team id member need role-owner

		return null;
	}

	@GetMapping("/me/{memberId}")
	public ResponseEntity<GetMyTeamsResponseDto> getMyTeams(
		@AuthenticationPrincipal CustomUserDetails user,
		@PathVariable("memberId") Long memberId
	) {

		// find my-teams by memberId
		if (Objects.equals(user.getId(), memberId)) {
			List<TeamDto> myTeams = teamService.getMyTeams(memberId);

			GetMyTeamsResponseDto responseDto = GetMyTeamsResponseDto.of(memberId, myTeams);

			return ResponseEntity.ok(responseDto);
		} else {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
	}

	@GetMapping("{teamId}/members")
	public ResponseEntity<TeamMembersResponseDto> getTeamMembers(
		@AuthenticationPrincipal CustomUserDetails user,
		@PathVariable("teamId") Long teamId
	) {

		// find teamMember by teamId

		return null;
	}

	@Override
	@GetMapping("/{teamId}")
	public ResponseEntity<TeamDto> getTeam(@AuthenticationPrincipal CustomUserDetails user,
		@PathVariable("teamId") Long teamId) {

		// 1) 인증 주체 확인 (선택: 스프링 시큐리티가 이미 인증 보장하면 생략 가능)
		if (user == null || user.getId() == null) {
			// 스프링 시큐리티 필터가 보통 401로 막지만, 방어적으로 한 번 더
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}

		if (teamService.checkTeamMember(user.getId(), teamId)) {

			TeamDto teamDto = teamService.getTeamDto(teamId);

			return ResponseEntity.ok(teamDto);

		} else {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
	}
}
