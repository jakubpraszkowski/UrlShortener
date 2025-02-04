package com.kubuski.urlshortener;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class UrlshortenerApplication {

    public static void main(final String[] args) {
        SpringApplication.run(UrlshortenerApplication.class, args);
    }
}
