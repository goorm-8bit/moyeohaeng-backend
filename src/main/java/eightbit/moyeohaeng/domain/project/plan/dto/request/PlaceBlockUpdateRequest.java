package eightbit.moyeohaeng.domain.project.plan.dto.request;

import java.time.LocalDate;
import java.time.LocalTime;

import org.hibernate.validator.constraints.URL;

import eightbit.moyeohaeng.domain.project.plan.entity.PlaceBlockType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

/**
 * PlaceBlock 수정 요청 DTO.
 * null 값이 아닌 필드만 갱신에 반영된다.
 */
public record PlaceBlockUpdateRequest(
	@Size(max = 100) String name,
	@Size(max = 255) String address,
	Double latitude,
	Double longitude,
	@Size(max = 2000) String memo,
	LocalDate date,
	LocalTime time,
	@URL @Size(max = 255) String reviewLink,
	@URL @Size(max = 255) String detailLink,
	@Size(max = 50) String category,
	@Size(max = 20) String color,
	@Email @Size(max = 120) String author,
	PlaceBlockType type
) {
}
