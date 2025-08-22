package eightbit.moyeohaeng.support.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.context.ImportTestcontainers;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

import eightbit.moyeohaeng.support.container.TestContainers;
import eightbit.moyeohaeng.support.util.JsonUtils;

@SpringBootTest
@AutoConfigureMockMvc
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@ImportTestcontainers(TestContainers.class)
@Import({JsonUtils.class})
@Transactional
public @interface IntegrationTest {
}
