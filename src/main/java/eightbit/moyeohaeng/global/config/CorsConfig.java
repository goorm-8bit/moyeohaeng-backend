package eightbit.moyeohaeng.global.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
public class CorsConfig {

	@Value("${cors.allowed-origins}")
	private String[] allowedOrigins;

	@Value("${cors.allowed-methods}")
	private String[] allowedMethods;

	@Value("${cors.allowed-headers}")
	private String[] allowedHeaders;

	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration sseConfig = new CorsConfiguration();
		sseConfig.setAllowedOrigins(List.of(allowedOrigins));
		sseConfig.setAllowedMethods(List.of("GET")); // SSE는 GET만 필요
		sseConfig.setAllowedHeaders(List.of(
			"Authorization",
			"Accept",
			"Cache-Control",
			"Last-Event-ID",
			"Connection"
		));
		sseConfig.setExposedHeaders(List.of("Last-Event-ID"));
		sseConfig.setAllowCredentials(true);

		CorsConfiguration config = new CorsConfiguration();
		config.setAllowedOrigins(List.of(allowedOrigins));
		config.setAllowedMethods(List.of(allowedMethods));
		config.setAllowedHeaders(List.of(allowedHeaders));
		config.setAllowCredentials(true);
		
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/v1/projects/*/connect", sseConfig);
		source.registerCorsConfiguration("/**", config);
		return source;
	}
}
