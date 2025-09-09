package eightbit.moyeohaeng.domain.project.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "실시간 접속 상태 응답 DTO")
public record PresenceDeleteResponse(
	@Schema(description = "식별자")
	String id
) {
	public static PresenceDeleteResponse of(String id) {
		return new PresenceDeleteResponse(id);
	}
}
