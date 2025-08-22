package eightbit.moyeohaeng.support.container;

import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;

public interface TestContainers {

	@Container
	@ServiceConnection
	MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.0.42");
}
