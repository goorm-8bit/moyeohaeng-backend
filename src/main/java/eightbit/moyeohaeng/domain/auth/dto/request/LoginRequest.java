package eightbit.moyeohaeng.domain.auth.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record LoginRequest(
	@Schema(description = "이메일", example = "moyeohaeng@test.com")
	@Email(message = "유효하지 않은 이메일 형식입니다.")
	@NotBlank(message = "이메일은 필수 입력 값입니다.")
	String email,

	@Schema(description = "비밀번호", example = "1Q2w3e4r!!")
	@NotBlank(message = "비밀번호는 필수 입력 값입니다.")
	@Size(min = 8, max = 20, message = "비밀번호는 8자 이상 20자 이하로 입력해주세요.")
	@Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,20}$",
		message = "비밀번호는 대문자, 소문자, 숫자, 특수문자를 각각 1자 이상 포함해야 합니다.")
	String password
) {

	public static LoginRequest of(String email, String password) {
		return new LoginRequest(email, password);
	}
}
