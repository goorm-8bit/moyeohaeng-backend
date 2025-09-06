package eightbit.moyeohaeng.global.event.sse;

import java.time.Duration;
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

	private static final int CACHE_LIMIT = 100;
	private static final Duration CACHE_TTL = Duration.ofMinutes(30);

	private final RedisTemplate<String, Object> redisTemplate;
	private final ObjectMapper objectMapper;

	public void cacheEvent(SseEmitterId id, MessageBody body) {
		String key = getKey(id);

		// 새 이벤트 추가
		redisTemplate.opsForZSet().add(key, body, body.timestamp());

		// 캐시 크기를 CACHE_LIMIT로 유지
		redisTemplate.opsForZSet().removeRange(key, 0, -(CACHE_LIMIT + 1));

		// 캐시 유효 기간 갱신
		redisTemplate.expire(key, CACHE_TTL);
	}

	public List<MessageBody> getMissedEvents(SseEmitterId id, String lastEventId) {
		String key = getKey(id);

		long timestamp = MessageBody.parseTimestamp(lastEventId);
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
