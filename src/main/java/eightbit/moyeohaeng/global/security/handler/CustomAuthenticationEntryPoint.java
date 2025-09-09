package eightbit.moyeohaeng.global.security.handler;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
        AuthenticationException authException) throws IOException, ServletException {
        
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");

        // RFC 6750/7235에 따라 WWW-Authenticate 헤더 추가
        String errorDescription = authException != null ? authException.getMessage() : "인증이 필요합니다";
        String wwwAuthHeader = String.format("Bearer realm=\"api\", error=\"invalid_token\", error_description=\"%s\"", errorDescription);
        response.setHeader("WWW-Authenticate", wwwAuthHeader);

        response.getWriter().write("{\"status\": 401, \"error\": {\"code\": \"UNAUTHORIZED\", \"message\": \"인증이 필요합니다.\"}}");
    }
}
