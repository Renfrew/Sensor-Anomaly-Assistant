package com.thinkquark.saa;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
class ApiJavaApplicationTests {
	@Container
	static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:18-alpine");

	@DynamicPropertySource
	static void registerProps(DynamicPropertyRegistry r) {
		r.add("spring.datasource.url", postgres::getJdbcUrl);
		r.add("spring.datasource.username", postgres::getUsername);
		r.add("spring.datasource.password", postgres::getPassword);
		r.add("spring.datasource.driver-class-name", () -> "org.postgresql.Driver");
		r.add("spring.jpa.hibernate.ddl-auto", () -> "none");
		// Flyway will run against this DB automatically if on classpath
	}

	@Test
	void contextLoads() {
	}

}
