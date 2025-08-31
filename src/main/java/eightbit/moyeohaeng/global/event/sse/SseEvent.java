package eightbit.moyeohaeng.global.event.sse;

import java.util.UUID;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public abstract class SseEvent {

	private final UUID uuid;
}
