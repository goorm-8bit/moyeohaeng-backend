package eightbit.moyeohaeng.domain.selection.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import eightbit.moyeohaeng.domain.selection.common.success.PlaceBlockCommentSuccessCode;
import eightbit.moyeohaeng.domain.selection.controller.swagger.PlaceBlockCommentApi;
import eightbit.moyeohaeng.domain.selection.dto.request.PlaceBlockCommentCreateRequest;
import eightbit.moyeohaeng.domain.selection.dto.request.PlaceBlockCommentUpdateRequest;
import eightbit.moyeohaeng.domain.selection.dto.response.PlaceBlockCommentResponse;
import eightbit.moyeohaeng.domain.selection.dto.response.PlaceBlockCommentSummaryResponse;
import eightbit.moyeohaeng.domain.selection.service.PlaceBlockCommentService;
import eightbit.moyeohaeng.global.security.CustomUserDetails;
import eightbit.moyeohaeng.global.success.SuccessResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/projects/{projectId}/place-blocks/{placeBlockId}/comments")
public class PlaceBlockCommentController implements PlaceBlockCommentApi {

	private final PlaceBlockCommentService commentService;

	@PostMapping
	@Override
	public SuccessResponse<PlaceBlockCommentResponse> create(
		@PathVariable Long projectId,
		@PathVariable Long placeBlockId,
		@Valid @RequestBody PlaceBlockCommentCreateRequest request,
		@AuthenticationPrincipal CustomUserDetails currentUser
	) {
		PlaceBlockCommentResponse response =
			commentService.create(projectId, placeBlockId, currentUser.getMemberId(), request);
		return SuccessResponse.of(PlaceBlockCommentSuccessCode.CREATE_COMMENT, response);
	}

	@PutMapping("/{commentId}")
	@Override
	public SuccessResponse<PlaceBlockCommentResponse> update(
		@PathVariable Long projectId,
		@PathVariable Long placeBlockId,
		@PathVariable Long commentId,
		@Valid @RequestBody PlaceBlockCommentUpdateRequest request,
		@AuthenticationPrincipal CustomUserDetails currentUser
	) {
		PlaceBlockCommentResponse response =
			commentService.update(projectId, placeBlockId, commentId, currentUser.getMemberId(), request);
		return SuccessResponse.of(PlaceBlockCommentSuccessCode.UPDATE_COMMENT, response);
	}

	@DeleteMapping("/{commentId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@Override
	public void delete(
		@PathVariable Long projectId,
		@PathVariable Long placeBlockId,
		@PathVariable Long commentId,
		@AuthenticationPrincipal CustomUserDetails currentUser
	) {
		commentService.delete(projectId, placeBlockId, commentId, currentUser.getMemberId());
	}

	@GetMapping
	@Override
	public SuccessResponse<List<PlaceBlockCommentResponse>> getComments(
		@PathVariable Long projectId,
		@PathVariable Long placeBlockId
	) {
		List<PlaceBlockCommentResponse> responses =
			commentService.getComments(projectId, placeBlockId);
		return SuccessResponse.of(PlaceBlockCommentSuccessCode.GET_COMMENT_LIST, responses);
	}

	@GetMapping("/summary")
	@Override
	public SuccessResponse<PlaceBlockCommentSummaryResponse> getCommentSummary(
		@PathVariable Long projectId,
		@PathVariable Long placeBlockId
	) {
		PlaceBlockCommentSummaryResponse summary =
			commentService.getCommentSummary(projectId, placeBlockId);
		return SuccessResponse.of(PlaceBlockCommentSuccessCode.GET_COMMENT_SUMMARY, summary);
	}
}
