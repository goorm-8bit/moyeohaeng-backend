package eightbit.moyeohaeng.domain.selection.controller.swagger;

import org.springframework.http.ResponseEntity;

import eightbit.moyeohaeng.domain.selection.dto.request.PlaceBlockCommentCreateRequest;
import eightbit.moyeohaeng.domain.selection.dto.request.PlaceBlockCommentUpdateRequest;
import eightbit.moyeohaeng.domain.selection.dto.response.PlaceBlockCommentResponse;
import eightbit.moyeohaeng.global.exception.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
// import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@Tag(name = "PlaceBlockComment", description = "장소 블록 댓글 API - 생성, 수정, 삭제")
// @SecurityRequirement(name = "bearerAuth")
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
	ResponseEntity<PlaceBlockCommentResponse> create(
		@Parameter(description = "프로젝트 ID", required = true, example = "1")
		Long projectId,
		@Parameter(description = "장소블록 ID", required = true, example = "10")
		Long placeBlockId,
		PlaceBlockCommentCreateRequest request
	);

	@Operation(summary = "장소 블록 코멘트 수정", description = "특정 댓글의 내용을 전체 갱신(PUT)합니다.")
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
	ResponseEntity<PlaceBlockCommentResponse> update(
		@Parameter(description = "프로젝트 ID", required = true, example = "1")
		Long projectId,
		@Parameter(description = "장소블록 ID", required = true, example = "10")
		Long placeBlockId,
		@Parameter(description = "댓글 ID", required = true, example = "100")
		Long commentId,
		PlaceBlockCommentUpdateRequest request
	);

	@Operation(summary = "장소 블록 코멘트 삭제", description = "특정 댓글을 삭제합니다. (소프트 삭제)")
	@ApiResponses({
		@ApiResponse(responseCode = "204", description = "삭제 성공"),
		@ApiResponse(responseCode = "403", description = "삭제 권한 없음",
			content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
		@ApiResponse(responseCode = "404", description = "프로젝트/장소블록/댓글 없음",
			content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
	})
	ResponseEntity<Void> delete(
		@Parameter(description = "프로젝트 ID", required = true, example = "1")
		Long projectId,
		@Parameter(description = "장소블록 ID", required = true, example = "10")
		Long placeBlockId,
		@Parameter(description = "댓글 ID", required = true, example = "100")
		Long commentId
	);
}
