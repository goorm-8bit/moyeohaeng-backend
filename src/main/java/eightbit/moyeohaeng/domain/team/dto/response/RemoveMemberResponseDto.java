package eightbit.moyeohaeng.domain.team.dto.response;

import eightbit.moyeohaeng.domain.team.dto.MemberDto;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record RemoveMemberResponseDto(
	@Schema(description = "삭제한 멤버를 전달함 따라서 이 객체로 조회 불가")
	@NotNull
	MemberDto targetMember
) {

}
