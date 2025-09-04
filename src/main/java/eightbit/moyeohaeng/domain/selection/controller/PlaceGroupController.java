package eightbit.moyeohaeng.domain.selection.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import eightbit.moyeohaeng.domain.selection.common.success.PlaceGroupSuccessCode;
import eightbit.moyeohaeng.domain.selection.controller.swagger.PlaceGroupApi;
import eightbit.moyeohaeng.domain.selection.dto.request.PlaceBlockToGroupsRequest;
import eightbit.moyeohaeng.domain.selection.dto.request.PlaceGroupRequest;
import eightbit.moyeohaeng.domain.selection.dto.request.PlaceGroupUpdateMemoRequest;
import eightbit.moyeohaeng.domain.selection.dto.response.PlaceGroupBlockResponse;
import eightbit.moyeohaeng.domain.selection.dto.response.PlaceGroupResponse;
import eightbit.moyeohaeng.domain.selection.dto.response.PlaceGroupUpdateMemoResponse;
import eightbit.moyeohaeng.domain.selection.service.PlaceGroupService;
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
		@Valid @RequestBody PlaceGroupRequest request
	) {
		PlaceGroupResponse response = placeGroupService.create(projectId, request);
		return SuccessResponse.of(PlaceGroupSuccessCode.CREATE_PLACE_GROUP, response);
	}

	@Override
	@PostMapping("/place-blocks/{placeBlockId}/place-groups")
	public SuccessResponse<PlaceGroupBlockResponse> updatePlaceBlockToGroups(
		@PathVariable Long projectId,
		@PathVariable Long placeBlockId,
		@Valid @RequestBody PlaceBlockToGroupsRequest request
	) {
		PlaceGroupBlockResponse response = placeGroupService.updatePlaceBlockToGroups(projectId, placeBlockId, request);
		return SuccessResponse.of(PlaceGroupSuccessCode.UPDATE_PLACE_BLOCK_TO_GROUPS, response);
	}

	@Override
	@PutMapping("/place-groups/{placeGroupId}")
	public SuccessResponse<PlaceGroupResponse> update(
		@PathVariable Long projectId,
		@PathVariable Long placeGroupId,
		@Valid @RequestBody PlaceGroupRequest request
	) {
		PlaceGroupResponse response = placeGroupService.update(projectId, placeGroupId, request);
		return SuccessResponse.of(PlaceGroupSuccessCode.UPDATE_PLACE_GROUP, response);
	}

	@Override
	@PutMapping("/place-groups/{placeGroupId}/memo")
	public SuccessResponse<PlaceGroupUpdateMemoResponse> updateMemo(
		@PathVariable Long projectId,
		@PathVariable Long placeGroupId,
		@Valid @RequestBody PlaceGroupUpdateMemoRequest request
	) {
		PlaceGroupUpdateMemoResponse response = placeGroupService.updateMemo(projectId, placeGroupId, request);
		return SuccessResponse.of(PlaceGroupSuccessCode.UPDATE_MEMO, response);
	}

	@Override
	@GetMapping("/place-groups")
	public SuccessResponse<List<PlaceGroupResponse>> getPlaceGroups(@PathVariable Long projectId) {
		List<PlaceGroupResponse> responses = placeGroupService.getPlaceGroups(projectId);
		return SuccessResponse.of(PlaceGroupSuccessCode.GET_LIST, responses);
	}

	@Override
	@DeleteMapping("/place-groups/{placeGroupId}")
	public SuccessResponse<Void> delete(@PathVariable Long projectId, @PathVariable Long placeGroupId) {
		placeGroupService.delete(projectId, placeGroupId);
		return SuccessResponse.from(PlaceGroupSuccessCode.DELETE_PLACE_GROUP);
	}
}
