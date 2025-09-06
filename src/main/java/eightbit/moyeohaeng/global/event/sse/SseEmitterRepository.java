package eightbit.moyeohaeng.global.event.sse;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Repository
public class SseEmitterRepository {

	private final Map<SseEmitterId, Map<UUID, SseEmitter>> emitters = new ConcurrentHashMap<>();

	public UUID save(SseEmitterId id, SseEmitter emitter) {
		UUID uuid = UUID.randomUUID();
		emitters.computeIfAbsent(id, key -> new ConcurrentHashMap<>())
			.put(uuid, emitter);

		return uuid;
	}

	public List<SseEmitter> findAll() {
		return emitters.values().stream()
			.flatMap(entry -> entry.values().stream())
			.toList();
	}

	public Map<UUID, SseEmitter> findAllById(SseEmitterId id) {
		return emitters.getOrDefault(id, Collections.emptyMap());
	}

	public void delete(SseEmitterId id, UUID uuid) {
		Map<UUID, SseEmitter> emittersById = emitters.get(id);

		if (!CollectionUtils.isEmpty(emittersById)) {
			emittersById.remove(uuid);

			if (emittersById.isEmpty()) {
				emitters.remove(id);
			}
		}
	}
}
