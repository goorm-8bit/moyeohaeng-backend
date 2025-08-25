package eightbit.moyeohaeng.global.config;


import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
	
	@Bean
	public OpenAPI openAPI() {
		return new OpenAPI()
			.info(apiInfo());
	}
	
	private Info apiInfo() {
		return new Info()
			.title("moyeohaeng")
			.description("moyeohaeng 의 Swagger API 명세서")
			.version("v1.0.0");
	}
	
	
}
