package eightbit.moyeohaeng.domain.selection.dto.request;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Schema(description = "장소 그룹 생성/수정 요청 DTO")
public record PlaceGroupRequest(
	@Schema(description = "이름", example = "점심 먹을 곳")
	@NotBlank(message = "이름은 필수 입력값입니다.")
	@Size(max = 20, message = "이름은 최대 20자까지 입력할 수 있습니다.")
	String name,

	@Schema(description = "색상", example = "#FFFFFF")
	@Pattern(regexp = "^#([A-Fa-f0-9]{6})$", message = "유효한 색상이 아닙니다.")
	@NotNull(message = "색상은 필수 입력값입니다.")
	String color,

	@Schema(description = "장소 블록 ID", example = "[1, 2, 3]")
	List<Long> placeBlockIds
) {
	// 컴팩트 생성자
	public PlaceGroupRequest {
		if (placeBlockIds == null) {
			placeBlockIds = List.of();
		}
	}
}
