package eightbit.moyeohaeng.domain.team.controller;

import eightbit.moyeohaeng.domain.team.dto.request.CreationTeamRequestDto;
import eightbit.moyeohaeng.domain.team.dto.request.InviteMemberRequestDto;
import eightbit.moyeohaeng.domain.team.dto.response.TeamMembersResponseDto;
import eightbit.moyeohaeng.domain.team.dto.request.UpdateMemberRoleRequestDto;
import eightbit.moyeohaeng.domain.team.dto.request.UpdateTeamSettingsRequestDto;
import eightbit.moyeohaeng.domain.team.dto.response.CreationTeamResponseDto;
import eightbit.moyeohaeng.domain.team.dto.response.DeleteTeamResponseDto;
import eightbit.moyeohaeng.domain.team.dto.response.InviteMemberResponseDto;
import eightbit.moyeohaeng.domain.team.dto.response.RemoveMemberResponseDto;
import eightbit.moyeohaeng.domain.team.dto.response.GetMyTeamsResponseDto;
import eightbit.moyeohaeng.domain.team.dto.response.UpdateMemberRoleResponseDto;
import eightbit.moyeohaeng.domain.team.dto.response.UpdateTeamSettingsResponseDto;
import eightbit.moyeohaeng.domain.team.service.TeamMemberService;
import eightbit.moyeohaeng.domain.team.service.TeamService;
import eightbit.moyeohaeng.global.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/teams")
@Slf4j
public class TeamController implements TeamApi{
	
	private final TeamService teamService;
	private final TeamMemberService teamMemberService;
	
	@Override
	public ResponseEntity<InviteMemberResponseDto> inviteMember(
		@AuthenticationPrincipal CustomUserDetails user,
		@RequestBody InviteMemberRequestDto requestDto
        ) {
		
		user.getId();
		
		return null;
	}
	
	@Override
	@PostMapping
	public ResponseEntity<CreationTeamResponseDto> creationTeam(
		@AuthenticationPrincipal CustomUserDetails user,
		@RequestBody CreationTeamRequestDto requestDto
        ) {
		
		user.getId();
		requestDto.newTeamName();
		
		
		
		
		return null;
	}
	
	@Override
	@DeleteMapping("/{teamId}/members/{memberId}")
	public ResponseEntity<RemoveMemberResponseDto> removeMember(
		@AuthenticationPrincipal CustomUserDetails user,
		@PathVariable("memberId") Long memberId
		) {
		
		
		
		// delete member in team by memberid need role = owner
		
		return null;
	}
	
	@Override
	@PutMapping("/{teamId}/members/{memberId}")
	public 	ResponseEntity<UpdateMemberRoleResponseDto> updateMemberRole(
		@AuthenticationPrincipal CustomUserDetails user,
        @RequestBody UpdateMemberRoleRequestDto requestDto,
		@PathVariable("memberId") Long memberId
        ) {
		
		// memberId 는 팀 멤버 권한을 재설정 하는 사람 (ex : 관리자)
		// 재설정을 받는 memberId 는 requestDto 에
		
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
		
		
		
		return null;
	}
	
	@GetMapping("{teamId}/members")
	public ResponseEntity<TeamMembersResponseDto> getTeamMembers(
		@AuthenticationPrincipal CustomUserDetails user,
        @PathVariable("teamId") Long teamId
	                                                            ) {
		
		// find teamMember by teamId
		
		return null;
	}
}
