package eightbit.moyeohaeng.domain.team.dto.response;

import eightbit.moyeohaeng.domain.team.dto.MemberDto;
import eightbit.moyeohaeng.domain.team.entity.TeamRole;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record UpdateMemberRoleResponseDto(
	@Schema(description = "변경 대상이 될 member", example = "dto")
	@NotNull
	MemberDto targetMember,
	@Schema(description = "새로 변경한 팀 ROLE", example = "OWNER or MEMBER 그 외 값은 오류")
	@NotBlank
	TeamRole newRole
) {

}
