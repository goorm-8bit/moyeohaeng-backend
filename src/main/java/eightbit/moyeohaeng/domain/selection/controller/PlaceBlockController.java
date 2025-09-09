package eightbit.moyeohaeng.domain.selection.controller;

import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import eightbit.moyeohaeng.domain.selection.common.success.PlaceBlockSuccessCode;
import eightbit.moyeohaeng.domain.selection.controller.swagger.PlaceBlockApi;
import eightbit.moyeohaeng.domain.selection.dto.request.PlaceBlockCreateRequest;
import eightbit.moyeohaeng.domain.selection.dto.request.PlaceBlockUpdateMemoRequest;
import eightbit.moyeohaeng.domain.selection.dto.response.PlaceBlockCreateResponse;
import eightbit.moyeohaeng.domain.selection.dto.response.PlaceBlockSearchResponse;
import eightbit.moyeohaeng.domain.selection.dto.response.PlaceBlockUpdateMemoResponse;
import eightbit.moyeohaeng.domain.selection.service.PlaceBlockService;
import eightbit.moyeohaeng.global.security.CustomUserDetails;
import eightbit.moyeohaeng.global.success.CommonSuccessCode;
import eightbit.moyeohaeng.global.success.SuccessResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/projects/{projectId}/place-blocks")
public class PlaceBlockController implements PlaceBlockApi {

	private final PlaceBlockService placeBlockService;

	@Override
	@PostMapping
	public SuccessResponse<PlaceBlockCreateResponse> create(
		@PathVariable Long projectId,
		@Valid @RequestBody PlaceBlockCreateRequest request
	) {
		PlaceBlockCreateResponse response = placeBlockService.create(projectId, request);
		return SuccessResponse.of(PlaceBlockSuccessCode.CREATE_PLACE_BLOCK, response);
	}

	@Override
	@PutMapping("/{placeBlockId}/memo")
	public SuccessResponse<PlaceBlockUpdateMemoResponse> updateMemo(
		@PathVariable Long projectId,
		@PathVariable Long placeBlockId,
		@Valid @RequestBody PlaceBlockUpdateMemoRequest request
	) {
		PlaceBlockUpdateMemoResponse response = placeBlockService.updateMemo(projectId, placeBlockId, request);
		return SuccessResponse.of(PlaceBlockSuccessCode.UPDATE_MEMO, response);
	}

	@Override
	@GetMapping
	public SuccessResponse<List<PlaceBlockSearchResponse>> searchPlaceBlocks(
		@PathVariable Long projectId,
		@AuthenticationPrincipal CustomUserDetails currentUser
	) {
		String username = currentUser.getUsername();
		List<PlaceBlockSearchResponse> responses = placeBlockService.searchPlaceBlocks(projectId, username);
		return SuccessResponse.of(PlaceBlockSuccessCode.SEARCH_LIST, responses);
	}

	@Override
	@DeleteMapping("/{placeBlockId}")
	public SuccessResponse<Void> delete(@PathVariable Long projectId, @PathVariable Long placeBlockId) {
		placeBlockService.delete(projectId, placeBlockId);
		return SuccessResponse.from(CommonSuccessCode.DELETE_SUCCESS);
	}
}
