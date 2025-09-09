package eightbit.moyeohaeng.domain.team.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record InviteMemberRequestDto(
	@Schema(description = "초대할 멤버의 ID", example = "123")
	@NotNull
	Long memberId
) {

}
