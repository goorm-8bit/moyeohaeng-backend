package eightbit.moyeohaeng.domain.team.dto;

import eightbit.moyeohaeng.domain.team.entity.Team;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;


@Builder
public record TeamDto(
	@Schema(description = "team id / Long 타입")
	@NotNull
	Long teamId,
	@Schema(description = "team name / 빈 문자열 null 공백 다 불가능")
	@NotBlank
	String teamName
) {

	public static TeamDto from(Team team) {
		return TeamDto.builder()
		              .teamId(team.getId())
		              .teamName(team.getName())
		              .build();
	}
}
