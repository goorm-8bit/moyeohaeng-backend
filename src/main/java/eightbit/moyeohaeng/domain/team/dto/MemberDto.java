package eightbit.moyeohaeng.domain.team.dto;

import eightbit.moyeohaeng.domain.member.entity.member.Member;
import eightbit.moyeohaeng.domain.team.entity.Team;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record MemberDto(
	@NotNull
	Long memberId,
	@NotBlank
	String memberName
) {
	
	public static MemberDto memberDto(Member member) {
		return MemberDto.builder()
						.memberId(member.getId())
						.memberName(member.getName())
		                .build();
	}
}
