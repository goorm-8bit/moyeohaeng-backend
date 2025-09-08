package eightbit.moyeohaeng.domain.team.dto.response;


import eightbit.moyeohaeng.domain.team.dto.MemberDto;
import java.util.List;

public record TeamMembersResponseDto(
	List<MemberDto> memberList
) { }
