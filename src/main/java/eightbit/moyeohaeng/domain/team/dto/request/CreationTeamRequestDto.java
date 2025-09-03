package eightbit.moyeohaeng.domain.team.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record CreationTeamRequestDto(
	@NotBlank(message = "팀 이름은 비어 있을 수 없습니다.")
	String teamName
) {
	
	public void validate() {
		if (teamName == null || teamName.isBlank()) {
			throw new IllegalArgumentException("teamName은 null이거나 빈 문자열일 수 없습니다.");
		}
	}
}
