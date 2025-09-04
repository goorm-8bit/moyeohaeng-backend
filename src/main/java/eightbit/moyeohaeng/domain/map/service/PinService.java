package eightbit.moyeohaeng.domain.map.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import eightbit.moyeohaeng.domain.map.common.exception.PinErrorCode;
import eightbit.moyeohaeng.domain.map.common.exception.PinException;
import eightbit.moyeohaeng.domain.map.dto.request.PinCreateRequest;
import eightbit.moyeohaeng.domain.map.dto.response.PinResponse;
import eightbit.moyeohaeng.domain.map.entity.Pin;
import eightbit.moyeohaeng.domain.map.repository.PinRepository;
import eightbit.moyeohaeng.domain.place.entity.Place;
import eightbit.moyeohaeng.domain.place.repository.PlaceRepository;
import eightbit.moyeohaeng.domain.project.common.exception.ProjectErrorCode;
import eightbit.moyeohaeng.domain.project.common.exception.ProjectException;
import eightbit.moyeohaeng.domain.project.entity.Project;
import eightbit.moyeohaeng.domain.project.repository.ProjectRepository;
import eightbit.moyeohaeng.global.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PinService {

	private final PinRepository pinRepository;
	private final ProjectRepository projectRepository;
	private final PlaceRepository placeRepository;

	/**
	 * 핀 생성
	 */
	@Transactional
	public PinResponse create(Long projectId, PinCreateRequest request, CustomUserDetails currentUser) {
		Project project = projectRepository.findById(projectId)
			.orElseThrow(() -> new ProjectException(ProjectErrorCode.PROJECT_NOT_FOUND));

		Place place = resolvePlace(request);

		// 중복 핀 검증
		validateDuplicatePin(projectId, place.getId());

		String author = (currentUser != null) ? currentUser.getEmail() : request.author();

		Pin saved = pinRepository.save(Pin.of(project, place, author));

		return PinResponse.from(saved);
	}

	/**
	 * 핀 조회
	 */
	public List<PinResponse> getPins(Long projectId) {
		return pinRepository.findAllByProjectId(projectId)
			.stream()
			.map(PinResponse::from)
			.toList();
	}

	/**
	 * 핀 삭제
	 */
	@Transactional
	public void delete(Long projectId, Long pinId) {
		Pin pin = pinRepository.findByIdAndProjectId(pinId, projectId)
			.orElseThrow(() ->
				new PinException(PinErrorCode.PIN_NOT_FOUND, "projectId=" + projectId, "pinId=" + pinId)
			);
		pinRepository.delete(pin);
	}

	/**
	 * 중복 핀 검증
	 */
	private void validateDuplicatePin(Long projectId, Long placeId) {
		if (pinRepository.existsByProjectIdAndPlaceId(projectId, placeId)) {
			throw new PinException(PinErrorCode.DUPLICATE_PIN,
				"projectId=" + projectId, "placeId=" + placeId);
		}
	}

	/**
	 * 주어진 좌표로 Place 조회, 없으면 생성 (위도/경도는 소수점 6자리 반올림 처리)
	 */
	private Place resolvePlace(PinCreateRequest req) {
		double latitude = roundToSixDecimals(req.latitude());
		double longitude = roundToSixDecimals(req.longitude());

		return placeRepository.findByLatitudeAndLongitude(latitude, longitude)
			.orElseGet(() -> placeRepository.save(
				Place.builder()
					.address(req.address())
					.latitude(latitude)
					.longitude(longitude)
					.detailLink(req.detailLink())
					.category(req.category())
					.build()
			));
	}

	private double roundToSixDecimals(Double value) {
		return new BigDecimal(value)
			.setScale(6, java.math.RoundingMode.HALF_UP)
			.doubleValue();
	}
}
