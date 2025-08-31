package eightbit.moyeohaeng.global.event.sse;

import java.time.Duration;
import java.util.Map;
import java.util.UUID;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import eightbit.moyeohaeng.global.event.message.MessageBody;
import eightbit.moyeohaeng.global.utils.GlobalLogger;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SseEmitterService {

	private static final Duration DEFAULT_TIMEOUT = Duration.ofMinutes(30);

	private final SseEmitterRepository sseEmitterRepository;
	private final ApplicationEventPublisher eventPublisher;

	public SseEmitter subscribe(ChannelTopic channel, Long eventId) {
		SseEmitter emitter = new SseEmitter(DEFAULT_TIMEOUT.toMillis());
		SseEmitterId id = SseEmitterId.of(channel.getTopic(), eventId);
		UUID uuid = sseEmitterRepository.save(id, emitter);

		// 이벤트 처리
		emitter.onTimeout(emitter::complete);
		emitter.onError((e) -> emitter.complete());
		emitter.onCompletion(() -> unsubscribe(id, uuid));

		// 이벤트 전송
		sendToClient(emitter, MessageBody.of("CONNECT", "hello"));
		eventPublisher.publishEvent(new SseSubscribeEvent(uuid));

		return emitter;
	}

	public void sendEvent(String channel, MessageBody messageBody) {
		SseEmitterId id = SseEmitterId.of(channel, messageBody.eventId());
		Map<UUID, SseEmitter> emittersById = sseEmitterRepository.findAllById(id);
		emittersById.forEach((uuid, emitter) -> sendToClient(emitter, messageBody));
	}

	private void sendToClient(SseEmitter emitter, MessageBody messageBody) {
		try {
			emitter.send(SseEmitter.event()
				.name(messageBody.eventName())
				.data(messageBody.payload()));
		} catch (Exception e) {
			GlobalLogger.error("SSE 이벤트 전송 실패", e);
		}
	}

	private void unsubscribe(SseEmitterId id, UUID uuid) {
		sseEmitterRepository.delete(id, uuid);
		eventPublisher.publishEvent(new SseUnsubscribeEvent(uuid));
	}
}
