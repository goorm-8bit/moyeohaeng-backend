package eightbit.moyeohaeng.global.event.sse;

import java.time.Duration;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "sse.cache")
public record SseCacheProperties(
	int limit,
	Duration ttl
) {
}
