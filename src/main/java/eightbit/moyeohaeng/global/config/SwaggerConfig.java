package eightbit.moyeohaeng.global.config;

import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;

@Configuration
@OpenAPIDefinition(
	info = @Info(
		title = "모여행 API",
		description = "모여행 REST API 문서",
		version = "v1.0.0",
		contact = @Contact(
			name = "Moyeohaeng Team",
			url = "https://github.com/goorm-8bit"
		)
	),
	servers = {
		@Server(url = "http://localhost:8080", description = "로컬 개발 서버"),
		@Server(url = "https://api.moyeohaeng.online", description = "운영 서버")
	},
	security = @SecurityRequirement(name = "bearerAuth")
)
@SecurityScheme(
	name = "bearerAuth",
	type = SecuritySchemeType.HTTP,
	scheme = "bearer",
	bearerFormat = "JWT",
	description = "JWT 토큰을 입력하세요 (Bearer 제외)"
)
public class SwaggerConfig {
}
