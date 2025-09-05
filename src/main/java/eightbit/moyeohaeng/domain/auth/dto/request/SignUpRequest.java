package eightbit.moyeohaeng.domain.auth.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record SignUpRequest(
	@Schema(description = "이메일", example = "moyeohaeng@test.com")
	@Email(message = "유효하지 않은 이메일 형식입니다.")
	@NotBlank(message = "이메일은 필수 입력 값입니다.")
	String email,

	@Schema(description = "비밀번호", example = "1Q2w3e4r!!")
	@NotBlank(message = "비밀번호는 필수 입력 값입니다.")
	@Size(min = 8, max = 20, message = "비밀번호는 8자 이상 20자 이하로 입력해주세요.")
	@Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,20}$",
		message = "비밀번호는 대문자, 소문자, 숫자, 특수문자를 각각 1자 이상 포함해야 합니다.")
	String password,

	@Schema(description = "이름", example = "모여행")
	@NotBlank(message = "이름은 필수 입력 값입니다.")
	@Size(min = 2, max = 10, message = "이름은 2자 이상 10자 이하로 입력해주세요.")
	@Pattern(regexp = "^[a-zA-Z가-힣]{2,10}$", message = "이름은 한글 또는 영어만 사용 가능하며, 특수문자와 공백은 사용할 수 없습니다.")
	String name
) {

	public static SignUpRequest of(String email, String password, String name) {
		return new SignUpRequest(email, password, name);
	}
}
