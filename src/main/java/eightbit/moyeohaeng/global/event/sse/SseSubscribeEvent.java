package eightbit.moyeohaeng.global.event.sse;

import java.util.UUID;

import lombok.Getter;

@Getter
public class SseSubscribeEvent extends SseEvent {

	private final String user;

	public SseSubscribeEvent(UUID uuid, String user) {
		super(uuid);
		this.user = user;
	}
}
