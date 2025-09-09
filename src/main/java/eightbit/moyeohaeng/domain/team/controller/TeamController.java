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
import org.springframework.web.bind.annotation.PatchMapping;
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

		InviteMemberResponseDto responseDto = teamService.inviteMember(requestDto.teamId(), user.getId(),
			requestDto.memberId());

		return ResponseEntity.ok(responseDto);
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
	@PatchMapping("/{teamId}/members/{memberId}/role")
	public ResponseEntity<UpdateMemberRoleResponseDto> updateMemberRole(
		@AuthenticationPrincipal CustomUserDetails user,
		@RequestBody UpdateMemberRoleRequestDto requestDto,
		@PathVariable("memberId") Long memberId,
		@PathVariable("teamId") Long teamId
	) {

		// userId 가 변경 권한이 있는지 확인
		// 재설정을 받는 memberId 는 PathVariable 에
		// 재설정 하는 teamId 는 PathVariable 에
		// 재설정 하는 권한이 뭔지는 requestDto 에
		UpdateMemberRoleResponseDto responseDto = teamService.updateMemberRole(teamId, user.getId(),
			memberId, requestDto.newRole());

		return ResponseEntity.ok(responseDto);
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

	@GetMapping("/me")
	public ResponseEntity<GetMyTeamsResponseDto> getMyTeams(
		@AuthenticationPrincipal CustomUserDetails user
	) {
		Long memberId = user.getId();
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
		// find teamMember by teamId and userId

		TeamMembersResponseDto responseDto = teamService.getTeamMembers(teamId, user.getId());

		return ResponseEntity.ok(responseDto);
	}

	@Override
	@GetMapping("/{teamId}")
	public ResponseEntity<TeamDto> getTeam(@AuthenticationPrincipal CustomUserDetails user,
		@PathVariable("teamId") Long teamId) {

		if (teamService.checkTeamMember(teamId, user.getId())) {

			TeamDto teamDto = teamService.getTeamDto(teamId);

			return ResponseEntity.ok(teamDto);

		} else {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
	}
}
