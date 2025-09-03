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
import eightbit.moyeohaeng.global.security.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Tags({
	@Tag(name="Team API", description = "Team 관련 조작에 쓰이는 API"),
//	@Tag(name="swagger 에 표시될 이름 2", description = "swagger 에 표시될 설명 2")
})
public interface TeamApi {
	
	@Operation(
		summary = "API 엔드 포인트에 대한 요약 정보",
		description = "API 엔드 포인트에 대한 자세한 설명",
		parameters = {
			@Parameter(
				name = "testParameter",
				in = ParameterIn.PATH,
				required = true,
				description = "테스트용 경로 변수",
				example = "hello"
			)
		}
	)
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "응답에 대한 설명",
		             content = @Content(mediaType = "application/json",
		                                schema = @Schema(implementation = String.class))),
		@ApiResponse(responseCode = "404", description = "응답에 대한 설명")
	})
	ResponseEntity<CreationTeamResponseDto> creationTeam(@AuthenticationPrincipal CustomUserDetails user,
	                                                     @RequestBody CreationTeamRequestDto requestDto);
	
	ResponseEntity<InviteMemberResponseDto> inviteMember(@AuthenticationPrincipal CustomUserDetails user,
	                                                    @RequestBody InviteMemberRequestDto requestDto);
	
	ResponseEntity<RemoveMemberResponseDto> removeMember(@AuthenticationPrincipal CustomUserDetails user,
	                                                     @RequestBody RemoveMemberRequestDto requestDto);
	
	ResponseEntity<UpdateMemberRoleResponseDto> updateMemberRole(@AuthenticationPrincipal CustomUserDetails user,
	                                                             @RequestBody UpdateMemberRoleRequestDto requestDto);
	
	ResponseEntity<UpdateTeamSettingsResponseDto> updateTeamSettings(@AuthenticationPrincipal CustomUserDetails user,
	                                                                 @RequestBody UpdateTeamSettingsRequestDto requestDto);
	
	ResponseEntity<DeleteTeamResponseDto> deleteTeam(@AuthenticationPrincipal CustomUserDetails user,
	                                                 @RequestBody DeleteTeamRequestDto requestDto);
	
	ResponseEntity<SearchMyTeamResponseDto> searchMyTeam(@AuthenticationPrincipal CustomUserDetails user,
	                                                     @RequestBody SearchMyTeamRequestDto requestDto);
}
