package eightbit.moyeohaeng.domain.team.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record UpdateTeamSettingsRequestDto(
	@NotBlank
	String newTeamName
) {

}
