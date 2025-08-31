package eightbit.moyeohaeng.support.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.junit.jupiter.api.extension.ExtendWith;

import eightbit.moyeohaeng.support.extension.RedisCleanupExtension;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@ExtendWith(RedisCleanupExtension.class)
public @interface CleanUpRedis {
}
