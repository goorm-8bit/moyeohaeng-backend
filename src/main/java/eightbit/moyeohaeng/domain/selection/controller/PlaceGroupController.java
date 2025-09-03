package eightbit.moyeohaeng.domain.selection.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import eightbit.moyeohaeng.domain.selection.common.success.PlaceGroupSuccessCode;
import eightbit.moyeohaeng.domain.selection.controller.swagger.PlaceGroupApi;
import eightbit.moyeohaeng.domain.selection.dto.request.PlaceGroupCreateRequest;
import eightbit.moyeohaeng.domain.selection.dto.response.PlaceGroupResponse;
import eightbit.moyeohaeng.domain.selection.service.PlaceGroupService;
import eightbit.moyeohaeng.global.security.CustomUserDetails;
import eightbit.moyeohaeng.global.success.SuccessResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/projects/{projectId}")
public class PlaceGroupController implements PlaceGroupApi {

	private final PlaceGroupService placeGroupService;

	@Override
	@PostMapping("/place-groups")
	public SuccessResponse<PlaceGroupResponse> create(
		@PathVariable Long projectId,
		@Valid @RequestBody PlaceGroupCreateRequest request,
		@AuthenticationPrincipal CustomUserDetails currentUser
	) {
		PlaceGroupResponse response = placeGroupService.create(projectId, request);
		return SuccessResponse.of(PlaceGroupSuccessCode.CREATE_PLACE_GROUP, response);
	}
}
