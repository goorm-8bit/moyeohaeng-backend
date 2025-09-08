package eightbit.moyeohaeng.domain.project.dto.response;

import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "실시간 접속 상태 응답 DTO")
public record PresenceDeleteResponse(
	@Schema(description = "식별자")
	UUID uuid
) {
	public static PresenceDeleteResponse of(UUID uuid) {
		return new PresenceDeleteResponse(uuid);
	}
}
