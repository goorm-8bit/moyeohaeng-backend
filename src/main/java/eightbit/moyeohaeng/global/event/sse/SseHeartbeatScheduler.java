package eightbit.moyeohaeng.global.event.sse;

import java.io.IOException;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import eightbit.moyeohaeng.global.utils.GlobalLogger;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SseHeartbeatScheduler {

	private final SseEmitterRepository sseEmitterRepository;

	/**
	 * 정해진 시간마다 모든 SSE 연결에 대해 하트비트 이벤트를 전송
	 */
	@Scheduled(fixedDelayString = "${sse.scheduling.heartbeat.fixed-rate}")
	public void sendHeartbeat() {
		List<SseEmitter> emitters = sseEmitterRepository.findAll();
		int unsubsribe = 0;

		for (SseEmitter emitter : emitters) {
			try {
				emitter.send(SseEmitter.event().comment("heartbeat"));
			} catch (IOException e) {
				unsubsribe++;
			}
		}

		GlobalLogger.info("[SSE] 프로젝트에 접속하고 있는 유저 수:", (emitters.size() - unsubsribe) + "명");
	}
}
