package eightbit.moyeohaeng.domain.map.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import eightbit.moyeohaeng.domain.map.controller.swagger.PinApi;
import eightbit.moyeohaeng.domain.map.dto.request.PinCreateRequest;
import eightbit.moyeohaeng.domain.map.dto.response.PinResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/v1/projects/{projectId}/pins")
@Validated
public class PinController implements PinApi {

	@PostMapping
	@Override
	public ResponseEntity<PinResponse> create(
		@PathVariable Long projectId,
		@Valid @RequestBody PinCreateRequest request
	) {
		// TODO: 구현 예정
		return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
	}

	@GetMapping
	@Override
	public ResponseEntity<List<PinResponse>> list(
		@PathVariable Long projectId
	) {
		// TODO: 구현 예정
		return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
	}

	@DeleteMapping("/{pinId}")
	@Override
	public ResponseEntity<Void> delete(
		@PathVariable Long projectId,
		@PathVariable Long pinId
	) {
		// TODO: 구현 예정
		return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
	}
}
