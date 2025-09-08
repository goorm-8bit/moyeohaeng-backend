package eightbit.moyeohaeng.domain.selection.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import eightbit.moyeohaeng.domain.selection.common.success.PlaceBlockLikeSuccessCode;
import eightbit.moyeohaeng.domain.selection.controller.swagger.PlaceBlockLikeApi;
import eightbit.moyeohaeng.domain.selection.dto.response.PlaceBlockLikeResponse;
import eightbit.moyeohaeng.domain.selection.dto.response.PlaceBlockLikeSummaryResponse;
import eightbit.moyeohaeng.domain.selection.service.PlaceBlockLikeService;
import eightbit.moyeohaeng.global.security.CustomUserDetails;
import eightbit.moyeohaeng.global.success.SuccessResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/v1/projects/{projectId}/place-blocks/{placeBlockId}/like")
@RequiredArgsConstructor
public class PlaceBlockLikeController implements PlaceBlockLikeApi {

	private final PlaceBlockLikeService placeBlockLikeService;

	@PostMapping
	@Override
	public SuccessResponse<PlaceBlockLikeResponse> toggleLike(
		@PathVariable Long projectId,
		@PathVariable Long placeBlockId,
		@AuthenticationPrincipal CustomUserDetails currentUser
	) {
		PlaceBlockLikeSummaryResponse summary =
			placeBlockLikeService.toggleLike(projectId, placeBlockId, currentUser);

		PlaceBlockLikeResponse response =
			PlaceBlockLikeResponse.of(placeBlockId, currentUser.getUsername(), summary.liked());

		return SuccessResponse.of(PlaceBlockLikeSuccessCode.TOGGLE_LIKE, response);
	}
}
