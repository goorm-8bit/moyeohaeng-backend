package eightbit.moyeohaeng.global.event.message;

public record MessageBody(
	Long eventId,
	String eventName,
	Object payload,
	long timestamp
) {
	public static MessageBody of(String eventName, Object payload) {
		return of(null, eventName, payload);
	}

	public static MessageBody of(Long eventId, String eventName, Object payload) {
		long timestamp = System.currentTimeMillis();
		return new MessageBody(eventId, eventName, payload, timestamp);
	}

	public static long parseTimestamp(String id) {
		try {
			String[] parts = id.split(":");
			return Long.parseLong(parts[parts.length - 1]);
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException("유효하지 않은 Last-Event-Id: " + id);
		}
	}

	public String getId() {
		return eventName + ":" + eventId + ":" + timestamp;
	}
}
