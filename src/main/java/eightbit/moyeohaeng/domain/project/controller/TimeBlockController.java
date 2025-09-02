package eightbit.moyeohaeng.domain.project.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import eightbit.moyeohaeng.domain.project.common.success.TimeBlockSuccessCode;
import eightbit.moyeohaeng.domain.project.controller.swagger.TimeBlockApi;
import eightbit.moyeohaeng.domain.project.dto.request.TimeBlockCreateRequest;
import eightbit.moyeohaeng.domain.project.dto.response.TimeBlockResponse;
import eightbit.moyeohaeng.domain.project.service.TimeBlockService;
import eightbit.moyeohaeng.global.security.CustomUserDetails;
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
		@Valid @RequestBody TimeBlockCreateRequest request,
		@AuthenticationPrincipal CustomUserDetails currentUser
	) {
		TimeBlockResponse response = timeBlockService.create(projectId, request);
		return SuccessResponse.of(TimeBlockSuccessCode.CREATE_TIME_BLOCK, response);
	}
}
