package eightbit.moyeohaeng.domain.team.dto.response;

import java.util.List;

import eightbit.moyeohaeng.domain.member.dto.MemberDto;

public record TeamMembersResponseDto(
	List<MemberDto> memberList
) {
}
