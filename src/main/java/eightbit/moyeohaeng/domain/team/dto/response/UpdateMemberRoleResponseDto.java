package eightbit.moyeohaeng.domain.team.dto.response;

import eightbit.moyeohaeng.domain.team.dto.MemberDto;
import eightbit.moyeohaeng.domain.team.entity.TeamRole;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record UpdateMemberRoleResponseDto(
	@NotNull
	MemberDto targetMember,
	@NotBlank
	TeamRole newRole
) {

}
