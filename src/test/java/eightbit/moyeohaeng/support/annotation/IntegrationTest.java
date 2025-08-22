package eightbit.moyeohaeng.support.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.context.ImportTestcontainers;
import org.springframework.transaction.annotation.Transactional;

import eightbit.moyeohaeng.support.container.TestContainers;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Retention(RetentionPolicy.RUNTIME)
@ImportTestcontainers(TestContainers.class)
@Transactional
public @interface IntegrationTest {
}
