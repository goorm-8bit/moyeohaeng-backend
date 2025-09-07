package eightbit.moyeohaeng.global.event.sse;

import java.util.UUID;

import lombok.Getter;

@Getter
public class SseSubscribeEvent extends SseEvent {

	private final String user;

	public SseSubscribeEvent(SseEmitterId id, UUID uuid, String user) {
		super(id, uuid);
		this.user = user;
	}
}
