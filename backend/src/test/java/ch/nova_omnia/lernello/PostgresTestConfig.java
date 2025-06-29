package ch.nova_omnia.lernello;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.PostgreSQLContainer;

@TestConfiguration
public class PostgresTestConfig {

    /**
     * IntelliJ shows a false warning:
     * "PostgreSQLContainer used without try-with-resources".
     * This is safe to suppress:
     * Spring Boot manages the container lifecycle via @ServiceConnection.
     */
    @SuppressWarnings("resource")
    @Bean
    @ServiceConnection
    public PostgreSQLContainer<?> postgresContainer() {
        return new PostgreSQLContainer<>("postgres:15")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");
    }
}
