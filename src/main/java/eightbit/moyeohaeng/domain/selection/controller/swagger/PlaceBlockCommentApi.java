package eightbit.moyeohaeng.domain.selection.controller.swagger;

import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;

import eightbit.moyeohaeng.domain.selection.dto.request.PlaceBlockCommentCreateRequest;
import eightbit.moyeohaeng.domain.selection.dto.request.PlaceBlockCommentUpdateRequest;
import eightbit.moyeohaeng.domain.selection.dto.response.PlaceBlockCommentResponse;
import eightbit.moyeohaeng.domain.selection.dto.response.PlaceBlockCommentSummaryResponse;
import eightbit.moyeohaeng.global.exception.ErrorResponse;
import eightbit.moyeohaeng.global.security.CustomUserDetails;
import eightbit.moyeohaeng.global.success.SuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "장소 블록 댓글 API", description = "장소 블록 댓글 CRUD 작업을 처리하는 API")
public interface PlaceBlockCommentApi {

	@Operation(summary = "장소 블록 코멘트 생성", description = "특정 장소 블록에 댓글을 생성합니다.")
	@ApiResponses({
		@ApiResponse(responseCode = "201", description = "댓글 생성 성공",
			content = @Content(schema = @Schema(implementation = PlaceBlockCommentResponse.class))),
		@ApiResponse(responseCode = "400", description = "잘못된 요청 데이터",
			content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
		@ApiResponse(responseCode = "404", description = "프로젝트/장소블록 없음",
			content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
	})
	SuccessResponse<PlaceBlockCommentResponse> create(
		@Parameter(description = "프로젝트 ID", required = true, example = "1")
		Long projectId,
		@Parameter(description = "장소블록 ID", required = true, example = "10")
		Long placeBlockId,
		PlaceBlockCommentCreateRequest request,
		@AuthenticationPrincipal CustomUserDetails currentUser
	);

	@Operation(summary = "장소 블록 코멘트 수정", description = "특정 댓글의 내용을 전체 갱신합니다.")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "수정 성공",
			content = @Content(schema = @Schema(implementation = PlaceBlockCommentResponse.class))),
		@ApiResponse(responseCode = "400", description = "잘못된 요청 데이터",
			content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
		@ApiResponse(responseCode = "403", description = "수정 권한 없음",
			content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
		@ApiResponse(responseCode = "404", description = "프로젝트/장소블록/댓글 없음",
			content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
	})
	SuccessResponse<PlaceBlockCommentResponse> update(
		@Parameter(description = "프로젝트 ID", required = true, example = "1")
		Long projectId,
		@Parameter(description = "장소블록 ID", required = true, example = "10")
		Long placeBlockId,
		@Parameter(description = "댓글 ID", required = true, example = "100")
		Long commentId,
		PlaceBlockCommentUpdateRequest request,
		@AuthenticationPrincipal CustomUserDetails currentUser
	);

	@Operation(summary = "장소 블록 코멘트 전체 조회", description = "특정 장소 블록의 모든 댓글을 조회합니다.")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "조회 성공",
			content = @Content(schema = @Schema(implementation = PlaceBlockCommentResponse.class))),
		@ApiResponse(responseCode = "404", description = "프로젝트/장소블록 없음",
			content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
	})
	SuccessResponse<List<PlaceBlockCommentResponse>> getComments(
		@Parameter(description = "프로젝트 ID", required = true, example = "1")
		Long projectId,
		@Parameter(description = "장소블록 ID", required = true, example = "10")
		Long placeBlockId
	);

	@Operation(summary = "장소 블록 코멘트 삭제", description = "특정 댓글을 삭제합니다. (소프트 삭제)")
	@ApiResponses({
		@ApiResponse(responseCode = "204", description = "삭제 성공"),
		@ApiResponse(responseCode = "403", description = "삭제 권한 없음",
			content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
		@ApiResponse(responseCode = "404", description = "프로젝트/장소블록/댓글 없음",
			content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
	})
	void delete(
		@Parameter(description = "프로젝트 ID", required = true, example = "1")
		Long projectId,
		@Parameter(description = "장소블록 ID", required = true, example = "10")
		Long placeBlockId,
		@Parameter(description = "댓글 ID", required = true, example = "100")
		Long commentId,
		@AuthenticationPrincipal CustomUserDetails currentUser
	);

	@Operation(summary = "장소 블록 코멘트 요약 조회", description = "총 댓글 개수와 마지막 댓글 정보를 조회합니다.")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "조회 성공",
			content = @Content(schema = @Schema(implementation = PlaceBlockCommentSummaryResponse.class))),
		@ApiResponse(responseCode = "404", description = "프로젝트/장소블록 없음",
			content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
	})
	SuccessResponse<PlaceBlockCommentSummaryResponse> getCommentSummary(
		@Parameter(description = "프로젝트 ID", required = true, example = "1")
		Long projectId,
		@Parameter(description = "장소블록 ID", required = true, example = "10")
		Long placeBlockId
	);
}
