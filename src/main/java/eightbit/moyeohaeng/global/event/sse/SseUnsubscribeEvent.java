package eightbit.moyeohaeng.global.event.sse;

import java.util.UUID;

public class SseUnsubscribeEvent extends SseEvent {

	public SseUnsubscribeEvent(UUID uuid) {
		super(uuid);
	}
}
