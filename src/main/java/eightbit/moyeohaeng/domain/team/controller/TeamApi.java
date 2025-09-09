package eightbit.moyeohaeng.domain.team.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

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

@Tags({
	@Tag(name = "Team API", description = "Team 관련 조작에 쓰이는 API"),
	//	@Tag(name="swagger 에 표시될 이름 2", description = "swagger 에 표시될 설명 2")
})
public interface TeamApi {

	@Operation(
		summary = "API 엔드 포인트에 대한 요약 정보",
		description = "API 엔드 포인트에 대한 자세한 설명",
		// @PathVariable 같은 것 설명 용도 body 를 Dto 로 받으면 Dto 에 @Schema 가 있어야 함
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
	ResponseEntity<CreateTeamResponseDto> createTeam(@AuthenticationPrincipal CustomUserDetails user,
		@RequestBody CreateTeamRequestDto requestDto);

	@Operation(
		summary = "초대 URL 을 보내서 초대하면 좋겠지만 시간이 없는 관계로 초대 할 대상 memberId 를 받는 것으로 구현",
		description = """
			초대할 대상의 memberId 를 받음
			현재 초대의 수락/거절 은 없고 memberId 입력시 바로 초대되는 형태로 구현 
			추후 수정 필요함
			"""
	)
	ResponseEntity<InviteMemberResponseDto> inviteMember(@AuthenticationPrincipal CustomUserDetails user,
		@RequestBody InviteMemberRequestDto requestDto);

	@Operation(
		summary = "ONWER 가 MEMBER 를 강퇴할 수 있음",
		description = "현재 미구현된 기능"
	)
	ResponseEntity<RemoveMemberResponseDto> removeMember(@AuthenticationPrincipal CustomUserDetails user,
		@PathVariable("memberId") Long memberId);

	@Operation(
		summary = "OWNER 가 MEMBER 의 권한을 재조정",
		parameters = {
			@Parameter(name = "teamId", in = ParameterIn.PATH, required = true,
				description = "멤버의 role 이 바뀌는 팀 ID", example = "Long type"),
			@Parameter(name = "memberId", in = ParameterIn.PATH, required = true,
				description = "role 이 바뀔 멤버 ID", example = "Long type"),
			@Parameter(name = "user", in = ParameterIn.PATH, hidden = true,
				description = "스프링 시큐리티가 넣어주는 값")
		}
	)
	ResponseEntity<UpdateMemberRoleResponseDto> updateMemberRole(@AuthenticationPrincipal CustomUserDetails user,
		@RequestBody UpdateMemberRoleRequestDto requestDto,
		@PathVariable("memberId") Long memberId,
		@PathVariable("teamId") Long teamId);

	@Operation(
		summary = "user 권한을 확인해서 팀 세팅을 바꿈 ",
		description = "현재 미구현된 기능"
	)
	ResponseEntity<UpdateTeamSettingsResponseDto> updateTeamSettings(@AuthenticationPrincipal CustomUserDetails user,
		@RequestBody UpdateTeamSettingsRequestDto requestDto,
		@PathVariable("teamId") Long teamId);

	@Operation(
		summary = "user 권한을 확인해서 팀을 삭제함",
		description = "현재 미구현된 기능"
	)
	ResponseEntity<DeleteTeamResponseDto> deleteTeam(@AuthenticationPrincipal CustomUserDetails user,
		@PathVariable Long teamId);

	@Operation(
		summary = "현재 로그인 유저의 id 를 이용해서 속한 팀 목록 출력"
	)
	ResponseEntity<GetMyTeamsResponseDto> getMyTeams(@AuthenticationPrincipal CustomUserDetails user,
		@PathVariable("memberId") Long memberId);

	@Operation(
		summary = "팀 id 를 이용해서 팀원 목록 출력"
	)
	ResponseEntity<TeamMembersResponseDto> getTeamMembers(@AuthenticationPrincipal CustomUserDetails user,
		@PathVariable("teamId") Long teamId);

	@Operation(
		summary = "팀 id 를 이용해서 팀 단건 출력"
	)
	ResponseEntity<TeamDto> getTeam(@AuthenticationPrincipal CustomUserDetails user,
		@PathVariable Long teamId);
}
