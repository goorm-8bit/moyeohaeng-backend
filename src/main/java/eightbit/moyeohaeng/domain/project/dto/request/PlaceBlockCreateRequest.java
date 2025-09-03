package eightbit.moyeohaeng.domain.project.dto.request;

import org.hibernate.validator.constraints.URL;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * PlaceBlock 생성 요청 DTO.
 * Controller에서 입력 검증 후 Entity로 변환한다.
 */
public record PlaceBlockCreateRequest(
	@NotBlank
	@Size(max = 100)
	String name,

	@Size(max = 255)
	String address,

	@NotNull
	@DecimalMin(value = "-90.0")
	@DecimalMax(value = "90.0")
	Double latitude,

	@NotNull
	@DecimalMin(value = "-180.0")
	@DecimalMax(value = "180.0")
	Double longitude,

	@Size(max = 2000)
	String memo,

	@URL
	@Size(max = 255)
	String detailLink,

	@Size(max = 50)
	String category,

	@Size(max = 20)
	String color,

	@Email
	@Size(max = 120)
	String author

) {
}
