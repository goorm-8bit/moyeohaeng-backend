package eightbit.moyeohaeng.global.event.sse;

import java.util.UUID;

import eightbit.moyeohaeng.global.dto.UserInfo;
import lombok.Getter;

@Getter
public class SseSubscribeEvent extends SseEvent {

	private final UserInfo userInfo;

	public SseSubscribeEvent(SseEmitterId id, UUID uuid, UserInfo userInfo) {
		super(id, uuid);
		this.userInfo = userInfo;
	}
}
