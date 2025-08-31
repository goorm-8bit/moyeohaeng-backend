package eightbit.moyeohaeng.support.container;

import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;

public interface TestContainers {

	@Container
	@ServiceConnection
	MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.0.42");
	
	@Container
	@ServiceConnection
	@SuppressWarnings("resource")
	GenericContainer<?> redis = new GenericContainer<>("redis:7.2")
		.withExposedPorts(6379);
}
