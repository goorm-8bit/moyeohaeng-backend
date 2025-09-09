package eightbit.moyeohaeng.domain.team.dto.request;

import eightbit.moyeohaeng.domain.team.entity.TeamRole;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record UpdateMemberRoleRequestDto(
	@Schema(description = "변경 요청할 TeamRole", example = "OWNER or MEMBER 그 외 값은 오류")
	@NotBlank
	TeamRole newRole
) {

}
