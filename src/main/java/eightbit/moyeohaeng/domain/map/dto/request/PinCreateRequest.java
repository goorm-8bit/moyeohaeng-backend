package eightbit.moyeohaeng.domain.map.dto.request;

import org.hibernate.validator.constraints.URL;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(description = "핀 생성 요청 DTO")
public record PinCreateRequest(

	@Schema(description = "주소", example = "부산광역시 해운대구 우동", maxLength = 200)
	@Size(max = 200)
	String address,

	@Schema(description = "위도(-90 ~ 90)", example = "35.1587")
	@NotNull
	@DecimalMin(value = "-90.0")
	@DecimalMax(value = "90.0")
	Double latitude,

	@Schema(description = "경도(-180 ~ 180)", example = "129.1604")
	@NotNull
	@DecimalMin(value = "-180.0")
	@DecimalMax(value = "180.0")
	Double longitude,

	@Schema(description = "상세 링크", example = "https://example.com/pin/1", maxLength = 250)
	@URL
	@Size(max = 250)
	String detailLink,

	@Schema(description = "카테고리", example = "맛집", maxLength = 30)
	@Size(max = 30)
	String category,

	@Schema(description = "기록자", example = "test@test.com", maxLength = 120)
	@Email
	@Size(max = 120)
	String author
) {
}
