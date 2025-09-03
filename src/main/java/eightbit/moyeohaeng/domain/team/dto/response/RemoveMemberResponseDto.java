package eightbit.moyeohaeng.domain.team.dto.response;

import eightbit.moyeohaeng.domain.team.dto.MemberDto;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record RemoveMemberResponseDto(
	@NotNull
	MemberDto targetMember
) {

}
