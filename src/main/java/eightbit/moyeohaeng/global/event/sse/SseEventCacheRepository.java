package eightbit.moyeohaeng.global.event.sse;

import java.util.Comparator;
import java.util.List;
import java.util.Set;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.databind.ObjectMapper;

import eightbit.moyeohaeng.global.event.message.MessageBody;
import eightbit.moyeohaeng.global.utils.GlobalLogger;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class SseEventCacheRepository {

	private final RedisTemplate<String, Object> redisTemplate;
	private final SseCacheProperties cacheProperties;
	private final ObjectMapper objectMapper;

	public void cacheEvent(SseEmitterId id, MessageBody body) {
		String key = getKey(id);

		// 새 이벤트 추가
		redisTemplate.opsForZSet().add(key, body, body.timestamp());

		// 캐시 크기 제한
		redisTemplate.opsForZSet().removeRange(key, 0, -(cacheProperties.limit() + 1));

		// 캐시 유효 기간 갱신
		redisTemplate.expire(key, cacheProperties.ttl());
	}

	public List<MessageBody> getMissedEvents(SseEmitterId id, String lastEventId) {
		String key = getKey(id);

		long timestamp;
		try {
			timestamp = MessageBody.parseTimestamp(id.eventId(), lastEventId);
		} catch (Exception e) {
			GlobalLogger.error("[SSE] Last-Event-ID 파싱 실패:", e.getMessage());
			return List.of();
		}

		Set<Object> events = redisTemplate.opsForZSet()
			.rangeByScore(key, timestamp + 1, Double.POSITIVE_INFINITY);

		if (events == null) {
			return List.of();
		}

		GlobalLogger.info("[SSE] 캐싱된 이벤트: size =", events.size(), ", id =", lastEventId);
		return events.stream()
			.map(o -> objectMapper.convertValue(o, MessageBody.class))
			.sorted(Comparator.comparingLong(MessageBody::timestamp))
			.toList();
	}

	private String getKey(SseEmitterId id) {
		return "SSE:CACHE:" + id.channel() + ":" + id.eventId();
	}
}
