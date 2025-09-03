package eightbit.moyeohaeng.domain.team.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record MemberDto(
	@NotNull
	Long memberId,
	@NotBlank
	String memberName
) {

}
