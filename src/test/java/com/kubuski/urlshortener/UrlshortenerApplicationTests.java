package com.kubuski.urlshortener;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import com.kubuski.urlshortener.repository.UrlRepository;
import com.kubuski.urlshortener.service.UrlService;

@SpringBootTest
@Testcontainers
public class UrlshortenerApplicationTests {

	@Autowired
	private ApplicationContext applicationContext;

	@Container
	public static PostgreSQLContainer<?> postgreSQLContainer =
			new PostgreSQLContainer<>("postgres:12").withDatabaseName("java_db:5432/prod_db")
					.withUsername("postgres").withPassword("postgres");

	@BeforeAll
	public static void startContainer() {
		postgreSQLContainer.start();
		System.out.println(
				"PostgreSQL container started with URL: " + postgreSQLContainer.getJdbcUrl());
	}

	@AfterAll
	public static void tearDownContainer() {
		if (postgreSQLContainer.isRunning())
			postgreSQLContainer.stop();
	}

	@DynamicPropertySource
	static void configureProperties(DynamicPropertyRegistry registry) {
		registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
		registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
		registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
	}

	@Test
	void contextLoads() {
		assertNotNull(applicationContext);
	}

	@Test
	void testUrlServiceBeanLoaded() {
		UrlService urlServiceBean = applicationContext.getBean(UrlService.class);
		assertNotNull(urlServiceBean);
	}

	@Test
	void testUrlRepositoryBeanLoaded() {
		UrlRepository urlRepositoryBean = applicationContext.getBean(UrlRepository.class);
		assertNotNull(urlRepositoryBean);
	}

	@Test
	void testMain() {
		UrlshortenerApplication.main(new String[] {});
	}
}
