package eightbit.moyeohaeng.global.team.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;

/**
 * 팀 관련 API를 정의하는 컨트롤러 인터페이스.
 *
 * 이 인터페이스에는 런타임 어노테이션을 붙이지 않고,
 * 구현체 클래스에서 Spring MVC 어노테이션을 적용합니다.
 * Swagger 문서화를 위한 @Operation, @ApiResponses 등은 유지합니다.
 */
@Tags({
	@Tag(name="swagger 에 표시될 이름 1", description = "swagger 에 표시될 설명 1"),
	@Tag(name="swagger 에 표시될 이름 2", description = "swagger 에 표시될 설명 2")
})
public interface TeamApi {
	
	/**
	 * 테스트용 엔드포인트.
	 * @param testParameter 테스트용 경로 변수 (예: 팀 ID 또는 식별자)
	 * @return 문자열 응답 (테스트용)
	 * 예상 동작 설명
	 */
	@Operation(
		summary = "API 엔드 포인트에 대한 요약 정보",
		description = "API 엔드 포인트에 대한 자세한 설명",
		parameters = {
			@Parameter(
				name = "testParameter",
				in = ParameterIn.PATH,
				required = true,
				description = "테스트용 경로 변수",
				example = "hello"
			)
		}
	)
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "응답에 대한 설명",
		             content = @Content(mediaType = "application/json",
		                                schema = @Schema(implementation = String.class))),
		@ApiResponse(responseCode = "404", description = "응답에 대한 설명")
	})
	// @RequestBody(description = "", required = true)
	// GET 에는 @RequestBody 가 필요하지 않음
	// 인터페이스 메소드 선언에는 어노테이션 ex)@PathVariable 등을 붙이지 않음
	String test( String testParameter);
}
