package eightbit.moyeohaeng.global.event.sse;

import java.util.UUID;

public class SseSubscribeEvent extends SseEvent {

	public SseSubscribeEvent(UUID uuid) {
		super(uuid);
	}
}
