package eightbit.moyeohaeng.global.event.message;

public record MessageBody(
	Long eventId,
	String eventName,
	Object payload
) {
	public static MessageBody of(String eventName, Object payload) {
		return new MessageBody(null, eventName, payload);
	}

	public static MessageBody of(Long eventId, String eventName, Object payload) {
		return new MessageBody(eventId, eventName, payload);
	}
}
