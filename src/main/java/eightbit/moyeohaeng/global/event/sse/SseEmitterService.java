package eightbit.moyeohaeng.global.event.sse;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import eightbit.moyeohaeng.global.event.message.MessageBody;
import eightbit.moyeohaeng.global.utils.GlobalLogger;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SseEmitterService {

	private static final Duration DEFAULT_TIMEOUT = Duration.ofMinutes(30);

	private final SseEmitterRepository sseEmitterRepository;
	private final SseEventCacheRepository sseEventCacheRepository;
	private final ApplicationEventPublisher eventPublisher;

	/**
	 * 지정된 채널과 이벤트에 대한 SSE 연결을 생성하고 구독하는 메서드
	 *
	 * @param channel 구독할 채널 (ex. PROJECT)
	 * @param eventId 구독할 특정 이벤트의 Id (ex. 프로젝트 Id)
	 * @param lastEventId 클라이언트가 마지막으로 수신한 이벤트 Id
	 * @param user    사용자 정보
	 * @return SseEmitter
	 */
	public SseEmitter subscribe(ChannelTopic channel, Long eventId, String lastEventId, String user) {
		SseEmitter emitter = new SseEmitter(DEFAULT_TIMEOUT.toMillis());
		SseEmitterId id = SseEmitterId.of(channel.getTopic(), eventId);
		UUID uuid = sseEmitterRepository.save(id, emitter);

		// 이벤트 처리
		emitter.onTimeout(emitter::complete);
		emitter.onError((e) -> emitter.complete());
		emitter.onCompletion(() -> unsubscribe(id, uuid));

		// Last-Event-ID가 존재하면, 캐시에 있는 이벤트 재전송
		if (StringUtils.hasText(lastEventId)) {
			List<MessageBody> missedEvents = sseEventCacheRepository.getMissedEvents(id, lastEventId);
			missedEvents.forEach(messageBody -> sendToClient(emitter, messageBody));
		}

		// 이벤트 전송
		sendToClient(emitter, MessageBody.of("CONNECT", "hello"));
		eventPublisher.publishEvent(new SseSubscribeEvent(uuid, user));

		return emitter;
	}

	public void sendEvent(String channel, MessageBody messageBody) {
		SseEmitterId id = SseEmitterId.of(channel, messageBody.eventId());
		Map<UUID, SseEmitter> emittersById = sseEmitterRepository.findAllById(id);
		emittersById.forEach((uuid, emitter) -> sendToClient(emitter, messageBody));

		// 캐시에 이벤트 저장
		sseEventCacheRepository.cacheEvent(id, messageBody);
	}

	private void sendToClient(SseEmitter emitter, MessageBody messageBody) {
		try {
			emitter.send(SseEmitter.event()
				.id(messageBody.getId())
				.name(messageBody.eventName())
				.data(messageBody.payload()));
		} catch (Exception e) {
			GlobalLogger.error("[SSE] 이벤트 전송 실패", e);
		}
	}

	private void unsubscribe(SseEmitterId id, UUID uuid) {
		sseEmitterRepository.delete(id, uuid);
		eventPublisher.publishEvent(new SseUnsubscribeEvent(uuid));
	}
}
