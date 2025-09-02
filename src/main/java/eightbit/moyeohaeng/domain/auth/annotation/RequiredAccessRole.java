package eightbit.moyeohaeng.domain.auth.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import eightbit.moyeohaeng.domain.auth.UserRole;

/**
 * Minimal access role required to access an endpoint across domains (Project/Team).
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface RequiredAccessRole {
	UserRole value();
}
