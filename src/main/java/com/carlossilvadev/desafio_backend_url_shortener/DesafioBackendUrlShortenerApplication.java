package com.carlossilvadev.desafio_backend_url_shortener;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

@SpringBootApplication
@EnableRedisRepositories
public class DesafioBackendUrlShortenerApplication {

	public static void main(String[] args) {
		SpringApplication.run(DesafioBackendUrlShortenerApplication.class, args);
	}

}
