package eightbit.moyeohaeng.global.security.handler;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
        AccessDeniedException accessDeniedException) throws IOException, ServletException {
        
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");

        // RFC 6750/7235에 따라 WWW-Authenticate 헤더 추가
        String errorDescription = accessDeniedException != null ? accessDeniedException.getMessage() : "인증이 필요합니다";
        String wwwAuthHeader = String.format("Bearer realm=\"api\", error=\"insufficient_scope\", error_description=\"%s\"", errorDescription);
        response.setHeader("WWW-Authenticate", wwwAuthHeader);

        response.getWriter().write("{\"status\": 401, \"error\": {\"code\": \"UNAUTHORIZED\", \"message\": \"인증이 필요합니다.\"}}");
    }
}
