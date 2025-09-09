package eightbit.moyeohaeng.domain.project.listener;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import eightbit.moyeohaeng.domain.project.service.PresenceService;
import eightbit.moyeohaeng.global.event.sse.SseSubscribeEvent;
import eightbit.moyeohaeng.global.event.sse.SseUnsubscribeEvent;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ProjectSseListener {

	private final PresenceService presenceService;

	@EventListener(condition = "#event.id.channel() == 'PROJECT'")
	public void handleSseSubscribeEvent(SseSubscribeEvent event) {
		Long projectId = event.getId().eventId();
		presenceService.subscribe(projectId, event.getUuid(), event.getUserInfo());
	}

	@EventListener(condition = "#event.id.channel() == 'PROJECT'")
	public void handleSseUnsubscribeEvent(SseUnsubscribeEvent event) {
		Long projectId = event.getId().eventId();
		presenceService.unsubscribe(projectId, event.getUuid());
	}
}
