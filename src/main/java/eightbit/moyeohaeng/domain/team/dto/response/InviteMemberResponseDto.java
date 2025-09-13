package eightbit.moyeohaeng.domain.team.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record InviteMemberResponseDto(
	@Schema(description = "초대할 멤버 ID", example = "123")
	@NotNull
	Long memberId,
	@Schema(description = "초대 하는 팀 ID", example = "123")
	@NotNull
	Long teamId
) {

}
