package com.kubuski.urlshortener;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
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
@Disabled
public class UrlshortenerApplicationTests {

	@Autowired
	private ApplicationContext applicationContext;

	@Container
	public static PostgreSQLContainer<?> postgreSQLContainer =
			new PostgreSQLContainer<>("postgres:12").withDatabaseName("java_db:5432/prod_db")
					.withUsername("postgres").withPassword("postgres");

	@BeforeAll
	public static void startContainer() {
        UrlshortenerApplicationTests.postgreSQLContainer.start();
		System.out.println(
				"PostgreSQL container started with URL: " + UrlshortenerApplicationTests.postgreSQLContainer.getJdbcUrl());
	}

	@AfterAll
	public static void tearDownContainer() {
		if (UrlshortenerApplicationTests.postgreSQLContainer.isRunning())
            UrlshortenerApplicationTests.postgreSQLContainer.stop();
	}

	@DynamicPropertySource
	static void configureProperties(final DynamicPropertyRegistry registry) {
		registry.add("spring.datasource.url", UrlshortenerApplicationTests.postgreSQLContainer::getJdbcUrl);
		registry.add("spring.datasource.username", UrlshortenerApplicationTests.postgreSQLContainer::getUsername);
		registry.add("spring.datasource.password", UrlshortenerApplicationTests.postgreSQLContainer::getPassword);
	}

	@Test
	void contextLoads() {
		assertNotNull(this.applicationContext);
	}

	@Test
	void testUrlServiceBeanLoaded() {
		final UrlService urlServiceBean = this.applicationContext.getBean(UrlService.class);
		assertNotNull(urlServiceBean);
	}

	@Test
	void testUrlRepositoryBeanLoaded() {
		final UrlRepository urlRepositoryBean = this.applicationContext.getBean(UrlRepository.class);
		assertNotNull(urlRepositoryBean);
	}

	@Test
	void testMain() {
		UrlshortenerApplication.main(new String[] {});
	}
}
