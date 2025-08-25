package eightbit.moyeohaeng.global.team.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class TeamController implements TeamApi {
	
	/**
	 * 테스트용 엔드포인트.
	 * @param testParameter 테스트용 경로 변수 (예: 팀 ID 또는 식별자)
	 * @return 문자열 응답 (테스트용)
	 * 예상 동작 설명
	 */
	// produces = "" 는 @RestController 어노테이션이 자동으로 mediatype 을 json 으로 해주지만 swagger 표시를 위해 붙임
	@Override
	@GetMapping(value = "/swagger/test/{testParameter}", produces = MediaType.APPLICATION_JSON_VALUE)
	public String test(@PathVariable("testParameter") String testParameter) {
		
		String str = testParameter;
		
		return "스웨거 값 테스트 test : " + str;
	}
}
