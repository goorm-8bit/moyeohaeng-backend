package eightbit.moyeohaeng.domain.project.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import eightbit.moyeohaeng.domain.project.controller.swagger.PlaceBlockCommentApi;
import eightbit.moyeohaeng.domain.project.dto.request.PlaceBlockCommentCreateRequest;
import eightbit.moyeohaeng.domain.project.dto.request.PlaceBlockCommentUpdateRequest;
import eightbit.moyeohaeng.domain.project.dto.response.PlaceBlockCommentResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/v1/projects/{projectId}/place-blocks/{placeBlockId}/comments")
@Validated
public class PlaceBlockCommentController implements PlaceBlockCommentApi {

	@PostMapping
	@Override
	public ResponseEntity<PlaceBlockCommentResponse> create(
		@PathVariable Long projectId,
		@PathVariable Long placeBlockId,
		@Valid @RequestBody PlaceBlockCommentCreateRequest request
	) {
		// TODO: 구현 예정
		return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
	}

	@PutMapping("/{commentId}")
	@Override
	public ResponseEntity<PlaceBlockCommentResponse> update(
		@PathVariable Long projectId,
		@PathVariable Long placeBlockId,
		@PathVariable Long commentId,
		@Valid @RequestBody PlaceBlockCommentUpdateRequest request
	) {
		// TODO: 구현 예정
		return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
	}

	@DeleteMapping("/{commentId}")
	@Override
	public ResponseEntity<Void> delete(
		@PathVariable Long projectId,
		@PathVariable Long placeBlockId,
		@PathVariable Long commentId
	) {
		// TODO: 구현 예정
		return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
	}
}
