package eightbit.moyeohaeng.domain.project.repository;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.databind.ObjectMapper;

import eightbit.moyeohaeng.global.dto.UserInfo;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class PresenceRepository {

	private static final Duration DEFAULT_TTL = Duration.ofMinutes(30);

	private final RedisTemplate<String, Object> redisTemplate;
	private final ObjectMapper objectMapper;

	public void savePresence(Long projectId, UUID uuid, UserInfo userInfo) {
		String key = getProjectKey(projectId);
		redisTemplate.opsForHash().put(key, uuid.toString(), userInfo);
		redisTemplate.expire(key, DEFAULT_TTL);
	}

	public List<UserInfo> findAllPresence(Long projectId) {
		String key = getProjectKey(projectId);
		Map<Object, Object> entries = redisTemplate.opsForHash().entries(key);

		return entries.values().stream()
			.map(this::convertUserInfo)
			.distinct()
			.toList();
	}

	public void deletePresence(Long projectId, UUID uuid) {
		String key = getProjectKey(projectId);
		redisTemplate.opsForHash().delete(key, uuid.toString());
	}

	private UserInfo convertUserInfo(Object value) {
		return objectMapper.convertValue(value, UserInfo.class);
	}

	private String getProjectKey(Long projectId) {
		return "PRESENCE:PROJECT:" + projectId;
	}
}
