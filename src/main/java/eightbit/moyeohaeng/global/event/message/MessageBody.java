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

	public static long parseTimestamp(Long eventId, String id) {
		try {
			String[] parts = id.split(":");
			if (Long.parseLong(parts[1]) != eventId) {
				throw new IllegalArgumentException("id가 일치하지 않습니다. eventId=" + eventId + ", id=" + id);
			}

			return Long.parseLong(parts[parts.length - 1]);
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException("유효하지 않은 Last-Event-ID (" + id + ")");
		}
	}

	public String getId() {
		return eventName + ":" + eventId + ":" + timestamp;
	}
}
