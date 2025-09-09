package eightbit.moyeohaeng.domain.team.dto.response;

import java.util.List;

import eightbit.moyeohaeng.domain.team.dto.MemberDto;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record TeamMembersResponseDto(
	@Schema(description = "멤버 리스트(비어있으면 멤버가 없는 것)", example = "List<MemberDto>")
	@NotNull
	List<MemberDto> memberList
) {
}
