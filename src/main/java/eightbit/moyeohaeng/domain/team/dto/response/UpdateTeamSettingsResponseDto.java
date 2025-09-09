package eightbit.moyeohaeng.domain.team.dto.response;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record UpdateTeamSettingsResponseDto(
	@NotBlank
	String newTeamName,
	@NotNull
	Long targetTeamId
) {

}
