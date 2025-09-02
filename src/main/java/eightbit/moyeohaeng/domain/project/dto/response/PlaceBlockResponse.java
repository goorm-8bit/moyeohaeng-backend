package eightbit.moyeohaeng.domain.project.dto.response;

import eightbit.moyeohaeng.domain.project.entity.PlaceBlock;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

/**
 * PlaceBlock 단건 응답 DTO.
 * Entity를 직렬화하여 API 응답으로 변환한다.
 */
@Schema(name = "PlaceBlockResponse", description = "장소 블록 단건 응답 DTO")
@Builder
public record PlaceBlockResponse(
	@Schema(description = "장소 블록 ID", example = "101")
	Long id,

	@Schema(description = "프로젝트 ID", example = "1")
	Long projectId,

	@Schema(description = "장소 이름", example = "성산일출봉")
	String name,

	@Schema(description = "주소", example = "제주특별자치도 서귀포시 성산읍 일출로 284-12")
	String address,

	@Schema(description = "위도", example = "33.4589")
	Double latitude,

	@Schema(description = "경도", example = "126.9421")
	Double longitude,

	@Schema(description = "메모(선택)", example = "일출 시간에 방문 추천")
	String memo,

	@Schema(description = "상세 링크(선택)", example = "https://map.naver.com/v5/entry/place/384169")
	String detailLink,

	@Schema(description = "카테고리", example = "관광지")
	String category,

	@Schema(description = "표시 색상 HEX", example = "#FF8A00")
	String color,

	@Schema(description = "작성자 이름", example = "minsu")
	String author
) {
	public static PlaceBlockResponse from(PlaceBlock e) {
		return PlaceBlockResponse.builder()
			.id(e.getId())
			.projectId(e.getProject().getId())
			.name(e.getName())
			.address(e.getAddress())
			.latitude(e.getLatitude())
			.longitude(e.getLongitude())
			.memo(e.getMemo())
			.detailLink(e.getDetailLink())
			.category(e.getCategory())
			.color(e.getColor())
			.author(e.getAuthor())
			.build();
	}
}
