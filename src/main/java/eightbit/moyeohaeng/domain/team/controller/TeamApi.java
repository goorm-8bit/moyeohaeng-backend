package eightbit.moyeohaeng.domain.team.controller;


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
	ResponseEntity<> creationTeam();
	
	ResponseEntity<> inviteMember();
	
	ResponseEntity<> removeMember();
	
	ResponseEntity<> updateMemberRole();
	
	ResponseEntity<> updateTeamSettings();
}
