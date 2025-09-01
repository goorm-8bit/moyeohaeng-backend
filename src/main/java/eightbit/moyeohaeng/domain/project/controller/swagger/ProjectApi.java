package eightbit.moyeohaeng.domain.project.controller.swagger;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import eightbit.moyeohaeng.domain.member.dto.response.MemberInfoResponse;
import eightbit.moyeohaeng.domain.project.dto.ProjectDto;
import eightbit.moyeohaeng.domain.project.dto.request.ProjectCreateRequest;
import eightbit.moyeohaeng.domain.project.dto.request.ProjectUpdateRequest;
import eightbit.moyeohaeng.global.exception.ErrorResponse;
import eightbit.moyeohaeng.global.security.CustomUserDetails;
import eightbit.moyeohaeng.global.success.SuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "프로젝트 API", description = "프로젝트 관련 CRUD 작업을 처리하는 API")
@SecurityRequirement(name = "Bearer Authentication")
public interface ProjectApi {

	@Operation(
		summary = "새 프로젝트 생성",
		description = "새로운 프로젝트를 생성합니다. 인증된 사용자만 접근 가능합니다."
	)
	@ApiResponses({
		@ApiResponse(
			responseCode = "201",
			description = "프로젝트 생성 성공",
			content = @Content(schema = @Schema(implementation = ProjectDto.class))
		),
		@ApiResponse(responseCode = "400", description = "잘못된 요청 파라미터",
			content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
		@ApiResponse(responseCode = "401", description = "인증 실패",
			content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
		@ApiResponse(responseCode = "403", description = "권한 없음",
			content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
	})
	@ResponseStatus(HttpStatus.CREATED)
	SuccessResponse<ProjectDto> create(ProjectCreateRequest request,
		@AuthenticationPrincipal CustomUserDetails currentUser);

	@Operation(
		summary = "프로젝트 수정",
		description = "특정 프로젝트의 정보를 수정합니다. 인증된 사용자만 접근 가능합니다."
	)
	@ApiResponses({
		@ApiResponse(
			responseCode = "200",
			description = "수정 성공",
			content = @Content(schema = @Schema(implementation = ProjectDto.class))
		),
		@ApiResponse(responseCode = "400", description = "잘못된 요청 파라미터",
			content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
		@ApiResponse(responseCode = "401", description = "인증 실패",
			content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
		@ApiResponse(responseCode = "403", description = "권한 없음",
			content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
		@ApiResponse(responseCode = "404", description = "프로젝트를 찾을 수 없음",
			content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
	})
	SuccessResponse<ProjectDto> update(
		@Parameter(name = "projectId", description = "수정할 프로젝트 ID", in = ParameterIn.PATH, required = true)
		Long projectId,
		ProjectUpdateRequest request,
		@AuthenticationPrincipal CustomUserDetails currentUser
	);

	@Operation(
		summary = "프로젝트 목록 조회",
		description = "프로젝트 목록을 조회합니다. 인증된 사용자만 접근 가능합니다."
	)
	@ApiResponses({
		@ApiResponse(
			responseCode = "200",
			description = "조회 성공",
			content = @Content(array = @ArraySchema(schema = @Schema(implementation = ProjectDto.class)))
		),
		@ApiResponse(responseCode = "401", description = "인증 실패",
			content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
	})
	SuccessResponse<List<ProjectDto>> get(@AuthenticationPrincipal CustomUserDetails currentUser);

	@Operation(
		summary = "특정 프로젝트 조회",
		description = "ID로 특정 프로젝트를 조회합니다. 인증된 사용자만 접근 가능합니다."
	)
	@ApiResponses({
		@ApiResponse(
			responseCode = "200",
			description = "조회 성공",
			content = @Content(schema = @Schema(implementation = ProjectDto.class))
		),
		@ApiResponse(responseCode = "401", description = "인증 실패",
			content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
		@ApiResponse(responseCode = "404", description = "프로젝트를 찾을 수 없음",
			content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
	})
	SuccessResponse<ProjectDto> getById(
		@Parameter(name = "projectId", description = "조회할 프로젝트 ID", in = ParameterIn.PATH, required = true)
		Long projectId,
		@AuthenticationPrincipal CustomUserDetails currentUser
	);

	@Operation(
		summary = "프로젝트 연결 및 SSE 구독",
		description = "특정 프로젝트와 관련된 테이블 정보를 조회하고, SSE(Server-Sent Events) 연결을 통해 실시간 알림을 구독합니다."
	)
	@ApiResponses({
		@ApiResponse(
			responseCode = "200",
			description = "연결 성공",
			content = @Content(mediaType = "text/event-stream")
		),
		@ApiResponse(responseCode = "401", description = "인증 실패",
			content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
		@ApiResponse(responseCode = "403", description = "권한 없음",
			content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
		@ApiResponse(responseCode = "404", description = "프로젝트를 찾을 수 없음",
			content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
	})
	SseEmitter connectProject(
		@Parameter(name = "projectId", description = "연결할 프로젝트 ID", in = ParameterIn.PATH, required = true)
		Long projectId,
		@AuthenticationPrincipal CustomUserDetails currentUser
	);

	@Operation(
		summary = "프로젝트에 접속한 멤버 목록",
		description = "프로젝트에 접속한 멤버들의 프로필, 이름, 이메일 조회, 인증된 사용자만 접근 가능합니다. "
	)
	@ApiResponses({
		@ApiResponse(
			responseCode = "200",
			description = "조회 성공",
			content = @Content(array = @ArraySchema(schema = @Schema(implementation = MemberInfoResponse.class)))

		),
		@ApiResponse(responseCode = "401", description = "인증 실패",
			content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
		@ApiResponse(responseCode = "403", description = "권한 없음",
			content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
		@ApiResponse(responseCode = "404", description = "멤버 목록을 찾을 수 없습니다.",
			content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
	})
	SuccessResponse<List<MemberInfoResponse>> getConnectedMember(
		@Parameter(name = "projectId", description = "연결할 프로젝트 ID", in = ParameterIn.PATH, required = true)
		Long projectId,
		@AuthenticationPrincipal CustomUserDetails currentUser
	);

	@Operation(
		summary = "프로젝트 삭제",
		description = "특정 프로젝트를 삭제합니다. 인증된 사용자만 접근 가능합니다."
	)
	@ApiResponses({
		@ApiResponse(
			responseCode = "204",
			description = "삭제 성공"
		),
		@ApiResponse(responseCode = "401", description = "인증 실패",
			content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
		@ApiResponse(responseCode = "403", description = "권한 없음",
			content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
		@ApiResponse(responseCode = "404", description = "프로젝트를 찾을 수 없음",
			content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
	})
	@ResponseStatus(HttpStatus.NO_CONTENT)
	SuccessResponse<Void> delete(
		@Parameter(name = "projectId", description = "삭제할 프로젝트 ID", in = ParameterIn.PATH, required = true)
		Long projectId,
		@AuthenticationPrincipal CustomUserDetails currentUser
	);
}
