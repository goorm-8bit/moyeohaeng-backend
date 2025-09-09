package eightbit.moyeohaeng.domain.project.dto.response;

import eightbit.moyeohaeng.domain.auth.UserRole;
import eightbit.moyeohaeng.global.dto.UserInfo;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "실시간 접속 상태 응답 DTO")
public record PresenceResponse(
	@Schema(description = "식별자")
	String id,

	@Schema(description = "이름", example = "모여행")
	String name,

	@Schema(description = "프로필 이미지")
	String profileImage,

	@Schema(description = "사용자 권한", example = "MEMBER")
	UserRole userRole
) {
	public static PresenceResponse of(UserInfo userInfo) {
		return new PresenceResponse(
			userInfo.email(), // TODO: 이메일에서 식별자로 대체하기
			userInfo.name(),
			userInfo.profileImage(),
			userInfo.userRole()
		);
	}
}
