package eightbit.moyeohaeng.global.event.sse;

public record SseEmitterId(
	String channel,
	Long eventId
) {
	public static SseEmitterId of(String channel, Long eventId) {
		return new SseEmitterId(channel, eventId);
	}
}
