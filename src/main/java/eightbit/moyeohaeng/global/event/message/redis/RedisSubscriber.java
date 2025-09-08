package eightbit.moyeohaeng.global.event.message.redis;

import java.nio.charset.StandardCharsets;

import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import eightbit.moyeohaeng.global.event.message.MessageBody;
import eightbit.moyeohaeng.global.event.sse.SseEmitterService;
import eightbit.moyeohaeng.global.utils.GlobalLogger;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RedisSubscriber implements MessageListener {

	private final ObjectMapper objectMapper;
	private final SseEmitterService sseEmitterService;

	@Override
	public void onMessage(@NonNull Message message, byte[] pattern) {
		try {
			String channel = new String(message.getChannel(), StandardCharsets.UTF_8);
			MessageBody messageBody = objectMapper.readValue(message.getBody(), MessageBody.class);
			sseEmitterService.sendEvent(channel, messageBody);
		} catch (Exception e) {
			GlobalLogger.error("Redis 메시지 처리 중 예외 발생", e);
		}
	}
}
