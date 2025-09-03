package eightbit.moyeohaeng.domain.team.controller;

import eightbit.moyeohaeng.domain.team.dto.request.CreationTeamRequestDto;
import eightbit.moyeohaeng.domain.team.dto.request.DeleteTeamRequestDto;
import eightbit.moyeohaeng.domain.team.dto.request.InviteMemberRequestDto;
import eightbit.moyeohaeng.domain.team.dto.request.RemoveMemberRequestDto;
import eightbit.moyeohaeng.domain.team.dto.request.SearchMyTeamRequestDto;
import eightbit.moyeohaeng.domain.team.dto.request.UpdateMemberRoleRequestDto;
import eightbit.moyeohaeng.domain.team.dto.request.UpdateTeamSettingsRequestDto;
import eightbit.moyeohaeng.domain.team.dto.response.CreationTeamResponseDto;
import eightbit.moyeohaeng.domain.team.dto.response.DeleteTeamResponseDto;
import eightbit.moyeohaeng.domain.team.dto.response.InviteMemberResponseDto;
import eightbit.moyeohaeng.domain.team.dto.response.RemoveMemberResponseDto;
import eightbit.moyeohaeng.domain.team.dto.response.SearchMyTeamResponseDto;
import eightbit.moyeohaeng.domain.team.dto.response.UpdateMemberRoleResponseDto;
import eightbit.moyeohaeng.domain.team.dto.response.UpdateTeamSettingsResponseDto;
import eightbit.moyeohaeng.domain.team.service.TeamService;
import eightbit.moyeohaeng.global.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
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
	
	@Override
	public ResponseEntity<InviteMemberResponseDto> inviteMember(
		@AuthenticationPrincipal CustomUserDetails user,
		@RequestBody InviteMemberRequestDto requestDto
        ) {
		
		return null;
	}
	
	@Override
	@PostMapping
	public ResponseEntity<CreationTeamResponseDto> creationTeam(
		@AuthenticationPrincipal CustomUserDetails user,
		@RequestBody CreationTeamRequestDto requestDto
        ) {
		
		user.getId();
		requestDto.teamName();
		
		
		return null;
	}
	
	@Override
	@DeleteMapping
	public ResponseEntity<RemoveMemberResponseDto> removeMember(
		@AuthenticationPrincipal CustomUserDetails user,
		@RequestBody RemoveMemberRequestDto requestDto
		) {
		
		return null;
		
	}
	
	@Override
	@PutMapping("/teamId")
	public 	ResponseEntity<UpdateMemberRoleResponseDto> updateMemberRole(
		@AuthenticationPrincipal CustomUserDetails user,
        @RequestBody UpdateMemberRoleRequestDto requestDto
        ) {
		return null;
	}
	
	public ResponseEntity<UpdateTeamSettingsResponseDto> updateTeamSettings(
		@AuthenticationPrincipal CustomUserDetails user,
		@RequestBody UpdateTeamSettingsRequestDto requestDto
        ) {
		return null;
	}
	
	public ResponseEntity<DeleteTeamResponseDto> deleteTeam(
	    @AuthenticationPrincipal CustomUserDetails user,
	    @RequestBody DeleteTeamRequestDto requestDto
        ) {
		return null;
	}
	
	public ResponseEntity<SearchMyTeamResponseDto> searchMyTeam(
		@AuthenticationPrincipal CustomUserDetails user,
		@RequestBody SearchMyTeamRequestDto requestDto
        ) {
		return null;
	}
}
