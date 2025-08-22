package eightbit.moyeohaeng.global.team.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TeamControllerImpl implements TeamController{

	@Override
	@GetMapping("/swagger/test/{testParameter}")
	public String test(@PathVariable("testParameter") String testParameter) {
		
		String str = testParameter;
		
		return "test : " + str;
	}
}
