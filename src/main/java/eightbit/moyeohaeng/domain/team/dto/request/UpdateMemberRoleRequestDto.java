package eightbit.moyeohaeng.domain.team.dto.request;

import eightbit.moyeohaeng.domain.team.entity.TeamRole;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record UpdateMemberRoleRequestDto(
	@NotBlank
	TeamRole updateRole,
	@NotNull
	Long targetMemberId
) {

}
