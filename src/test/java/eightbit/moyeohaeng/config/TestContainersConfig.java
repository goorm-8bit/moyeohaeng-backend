package eightbit.moyeohaeng.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.beans.factory.DisposableBean;

import javax.sql.DataSource;

import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.utility.DockerImageName;

/**
 * TestContainers를 이용한 MySQL 설정
 * Spring 컨텍스트 종료 시 자동으로 리소스를 정리하도록 DisposableBean 구현
 */
@TestConfiguration
public class TestContainersConfig implements DisposableBean {

    /**
     * MySQL 컨테이너를 싱글톤으로 관리하는 내부 클래스
     */
    private static class MySQLContainerHolder {
        private static final MySQLContainer<?> INSTANCE;
        
        static {
            MySQLContainer<?> container = new MySQLContainer<>(DockerImageName.parse("mysql:8.0"))
                    .withDatabaseName("testdb")
                    .withUsername("test")
                    .withPassword("test")
                    .withCommand("--character-set-server=utf8mb4", "--collation-server=utf8mb4_unicode_ci")
                    .withReuse(true);
            
            try {
                container.start();
                // Add JVM shutdown hook for cleanup
                Runtime.getRuntime().addShutdownHook(new Thread(container::close));
            } catch (Exception e) {
                // Ensure container is closed if startup fails
                container.close();
                throw e;
            }
            INSTANCE = container;
        }
        
        public static MySQLContainer<?> getInstance() {
            return INSTANCE;
        }
    }
    
    /**
     * MySQL 컨테이너를 빈으로 등록
     */
    @Bean
    @ServiceConnection
    public MySQLContainer<?> mysqlContainer() {
        return MySQLContainerHolder.getInstance();
    }
    
    /**
     * 데이터소스 빈 생성
     */
    @Bean
    public DataSource dataSource() {
        MySQLContainer<?> container = MySQLContainerHolder.getInstance();
        return DataSourceBuilder.create()
                .url(container.getJdbcUrl())
                .username(container.getUsername())
                .password(container.getPassword())
                .driverClassName(container.getDriverClassName())
                .build();
    }
    
    /**
     * Spring 컨텍스트 종료 시 컨테이너 정리
     */
    @Override
    public void destroy() {
        MySQLContainer<?> container = MySQLContainerHolder.getInstance();
        if (container != null && container.isRunning()) {
            container.stop();
        }
    }
}
