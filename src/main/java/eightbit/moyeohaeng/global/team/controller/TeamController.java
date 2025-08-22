package eightbit.moyeohaeng.global.team.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
import org.springframework.web.bind.annotation.PathVariable;

@Tags({
	@Tag(name="swagger 에 표시될 이름 1", description = "swagger 에 표시될 설명 1"),
	@Tag(name="swagger 에 표시될 이름 2", description = "swagger 에 표시될 설명 2")
})
public interface TeamController {
	
	@Operation(
		summary = "API 엔드 포인트에 대한 요약 정보",
		description = "API 엔드 포인트에 대한 자세한 설명",
		parameters = {
			@Parameter(
				name = "testParameter",
				in = ParameterIn.PATH,
				required = true,
				description = "테스트용 경로 변수"
			)
		}
	)
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "응답에 대한 설명"),
		@ApiResponse(responseCode = "404", description = "응답에 대한 설명")
	})
	// @RequestBody(description = "", required = true)
	// GET 에는 @RequestBody 가 필요하지 않음
	// 인터페이스 메소드 선언에는 어노테이션 ex)@PathVariable 등을 붙이지 않음
	String test( String testParameter);
}
