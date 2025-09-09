package eightbit.moyeohaeng.domain.member.dto.request;

public record MemberUpdateRequest(
	String name,
	String profileImage,
	String password
) {
}
