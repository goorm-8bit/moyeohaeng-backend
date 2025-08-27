package eightbit.moyeohaeng.config;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * 통합 테스트를 위한 기본 설정 클래스
 * - TestContainers를 사용하여 MySQL 데이터베이스를 설정
 * - MockMvc를 사용하여 HTTP 요청 테스트
 * - AssertJ를 사용하여 검증
 */
@SpringBootTest
@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@ContextConfiguration(classes = {TestContainersConfig.class, TestSecurityConfig.class})
public abstract class AbstractIntegrationTest {
    // 공통 유틸리티 메서드는 여기에 추가
}
