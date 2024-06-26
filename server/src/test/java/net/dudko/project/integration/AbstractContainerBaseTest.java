package net.dudko.project.integration;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;

public abstract class AbstractContainerBaseTest {

    public static final PostgreSQLContainer POSTGRESQL_CONTAINER;

    static {
        POSTGRESQL_CONTAINER = new PostgreSQLContainer("postgres:latest")
                .withDatabaseName("ems")
                .withUsername("postgres")
                .withPassword("postgres");
        POSTGRESQL_CONTAINER.start();
    }

    @DynamicPropertySource
    private static void dynamicPropertySource(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", POSTGRESQL_CONTAINER::getJdbcUrl);
        registry.add("spring.datasource.username", POSTGRESQL_CONTAINER::getUsername);
        registry.add("spring.datasource.password", POSTGRESQL_CONTAINER::getPassword);
    }

}
