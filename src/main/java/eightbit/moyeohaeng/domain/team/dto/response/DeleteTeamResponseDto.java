package eightbit.moyeohaeng.domain.team.dto.response;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record DeleteTeamResponseDto(
	@NotBlank
	String teamName,
	@NotNull
	Long targetTeamId
) {

}
