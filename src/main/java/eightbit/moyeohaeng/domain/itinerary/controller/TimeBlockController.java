package eightbit.moyeohaeng.domain.itinerary.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import eightbit.moyeohaeng.domain.itinerary.common.success.TimeBlockSuccessCode;
import eightbit.moyeohaeng.domain.itinerary.controller.swagger.TimeBlockApi;
import eightbit.moyeohaeng.domain.itinerary.dto.request.TimeBlockCreateRequest;
import eightbit.moyeohaeng.domain.itinerary.dto.request.TimeBlockUpdateRequest;
import eightbit.moyeohaeng.domain.itinerary.dto.response.TimeBlockResponse;
import eightbit.moyeohaeng.domain.itinerary.service.TimeBlockService;
import eightbit.moyeohaeng.global.success.SuccessResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/projects/{projectId}/time-blocks")
public class TimeBlockController implements TimeBlockApi {

	private final TimeBlockService timeBlockService;

	@Override
	@PostMapping
	public SuccessResponse<TimeBlockResponse> create(
		@PathVariable Long projectId,
		@Valid @RequestBody TimeBlockCreateRequest request
	) {
		TimeBlockResponse response = timeBlockService.create(projectId, request);
		return SuccessResponse.of(TimeBlockSuccessCode.CREATE_TIME_BLOCK, response);
	}

	@Override
	@PutMapping("/{timeBlockId}")
	public SuccessResponse<TimeBlockResponse> update(
		@PathVariable Long projectId,
		@PathVariable Long timeBlockId,
		@Valid @RequestBody TimeBlockUpdateRequest request
	) {
		TimeBlockResponse response = timeBlockService.update(projectId, timeBlockId, request);
		return SuccessResponse.of(TimeBlockSuccessCode.UPDATE_TIME_BLOCK, response);
	}

	@Override
	@GetMapping
	public SuccessResponse<List<TimeBlockResponse>> getTimeBlocks(
		@PathVariable Long projectId,
		@RequestParam(required = false) Integer day
	) {
		List<TimeBlockResponse> responses = timeBlockService.getTimeBlocks(projectId, day);
		return SuccessResponse.of(TimeBlockSuccessCode.GET_LIST, responses);
	}
}
