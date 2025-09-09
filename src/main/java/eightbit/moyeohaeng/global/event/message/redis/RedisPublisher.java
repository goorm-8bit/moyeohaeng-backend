package eightbit.moyeohaeng.global.event.message.redis;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import eightbit.moyeohaeng.global.event.message.MessageBody;
import eightbit.moyeohaeng.global.event.message.MessagePublisher;
import eightbit.moyeohaeng.global.utils.GlobalLogger;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RedisPublisher implements MessagePublisher {

	private final RedisTemplate<String, Object> redisTemplate;

	@Override
	public void convertAndSend(String channel, MessageBody body) {
		try {
			Long receivers = redisTemplate.convertAndSend(channel, body);
			GlobalLogger.info("[REDIS] 메시지 발행: id =", body.getId(), ", receivers =", receivers);
		} catch (Exception e) {
			GlobalLogger.error("Redis 메시지 전송 실패", e);
		}
	}
}
