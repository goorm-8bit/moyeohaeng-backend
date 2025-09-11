package eightbit.moyeohaeng.domain.map.controller;

import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import eightbit.moyeohaeng.domain.auth.UserRole;
import eightbit.moyeohaeng.domain.auth.common.annotation.RequiredAccessRole;
import eightbit.moyeohaeng.domain.map.common.success.PinSuccessCode;
import eightbit.moyeohaeng.domain.map.controller.swagger.PinApi;
import eightbit.moyeohaeng.domain.map.dto.request.PinCreateRequest;
import eightbit.moyeohaeng.domain.map.dto.response.PinResponse;
import eightbit.moyeohaeng.domain.map.service.PinService;
import eightbit.moyeohaeng.global.security.CustomUserDetails;
import eightbit.moyeohaeng.global.success.SuccessResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/projects/{projectId}/pins")
public class PinController implements PinApi {

	private final PinService pinService;

	@PostMapping
	@Override
	@RequiredAccessRole(UserRole.GUEST)
	public SuccessResponse<PinResponse> create(
		@PathVariable Long projectId,
		@Valid @RequestBody PinCreateRequest request,
		@AuthenticationPrincipal CustomUserDetails currentUser
	) {
		String email = (currentUser != null ? currentUser.getEmail() : null);
		PinResponse response = pinService.create(projectId, request, email);
		return SuccessResponse.of(PinSuccessCode.CREATE_PIN, response);
	}

	@GetMapping
	@Override
	@RequiredAccessRole(UserRole.VIEWER)
	public SuccessResponse<List<PinResponse>> getPins(@PathVariable Long projectId) {
		List<PinResponse> responses = pinService.getPins(projectId);
		return SuccessResponse.of(PinSuccessCode.GET_PIN_LIST, responses);
	}

	@DeleteMapping("/{pinId}")
	@Override
	@RequiredAccessRole(UserRole.GUEST)
	public SuccessResponse<Void> delete(
		@PathVariable Long projectId,
		@PathVariable Long pinId
	) {
		pinService.delete(projectId, pinId);
		return SuccessResponse.from(PinSuccessCode.DELETE_PIN);
	}
}
