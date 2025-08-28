package eightbit.moyeohaeng.domain.project.dto.request;

import org.hibernate.validator.constraints.URL;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

/**
 * PlaceBlock 수정 요청 DTO.
 * null 값이 아닌 필드만 갱신에 반영된다.
 * 시간은 사용자의 로컬 타임존(Asia/Seoul) 기준으로 해석되며, 서버 측 변환은 수행하지 않습니다.
 */
public record PlaceBlockUpdateRequest(
	@Size(max = 100)
	String name,

	@Size(max = 255)
	String address,

	@DecimalMin(value = "-90.0")
	@DecimalMax(value = "90.0")
	Double latitude,

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
