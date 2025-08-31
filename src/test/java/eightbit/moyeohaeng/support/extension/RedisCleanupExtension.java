package eightbit.moyeohaeng.support.extension;

import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;

public class RedisCleanupExtension implements AfterEachCallback {

	@Override
	public void afterEach(ExtensionContext context) {
		RedisTemplate<String, ?> redisTemplate = getRedisTemplate(context);

		redisTemplate.execute((RedisConnection connection) -> {
			connection.serverCommands().flushAll();
			return null;
		});
	}

	@SuppressWarnings("unchecked")
	private RedisTemplate<String, ?> getRedisTemplate(ExtensionContext context) {
		return (RedisTemplate<String, ?>)SpringExtension.getApplicationContext(context)
			.getBean("redisTemplate");
	}
}
