package eightbit.moyeohaeng.domain.selection.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import eightbit.moyeohaeng.domain.selection.controller.swagger.PlaceBlockLikeApi;
import eightbit.moyeohaeng.domain.selection.dto.response.PlaceBlockLikeSummaryResponse;
import jakarta.validation.constraints.NotNull;

@RestController
@RequestMapping("/v1/projects/{projectId}/place-blocks/{placeBlockId}/like")
@Validated
public class PlaceBlockLikeController implements PlaceBlockLikeApi {

	@PostMapping
	@Override
	public ResponseEntity<PlaceBlockLikeSummaryResponse> toggle(
		@PathVariable @NotNull Long projectId,
		@PathVariable @NotNull Long placeBlockId
	) {
		// TODO: 구현 예정 (토글 처리: 존재하면 soft-delete 복구/삭제 전환)
		return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
	}
}
