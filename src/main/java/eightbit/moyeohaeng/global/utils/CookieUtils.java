package eightbit.moyeohaeng.global.utils;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

import org.springframework.util.StringUtils;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

public class CookieUtils {

	public static Optional<String> getCookieValue(HttpServletRequest request, String cookieName) {
		if (!StringUtils.hasText(cookieName)) {
			return Optional.empty();
		}

		Cookie[] cookies = request.getCookies();
		return Arrays.stream(cookies)
			.filter(cookie -> Objects.equals(cookieName, cookie.getName()))
			.map(Cookie::getValue)
			.findFirst();
	}
}
