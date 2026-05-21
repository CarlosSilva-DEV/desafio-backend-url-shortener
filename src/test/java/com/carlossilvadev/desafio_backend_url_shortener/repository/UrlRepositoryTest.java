package com.carlossilvadev.desafio_backend_url_shortener.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.redis.test.autoconfigure.DataRedisTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.carlossilvadev.desafio_backend_url_shortener.config.RedisConfig;
import com.carlossilvadev.desafio_backend_url_shortener.model.Url;

@DataRedisTest
@Testcontainers
@Import(RedisConfig.class)
@ActiveProfiles("test")
public class UrlRepositoryTest {
	
	@Container // cria um container do Redis para testes de integração
	@ServiceConnection @SuppressWarnings("resource")
	static final GenericContainer<?> REDIS_CONTAINER = new GenericContainer<>("redis:8.2-alpine").withExposedPorts(6379);
	
	@Autowired
	private UrlRepository repository;
	
	@AfterEach // ao final de cada teste, os dados são removidos do Redis, garantindo isolamento entre os testes
	void tearDown() {
		repository.deleteAll();
	}
	
	@Test
	@DisplayName("Deve salvar e recuperar uma entidade Url através da chave shortenedUrl")
	void shouldSaveAndFindByShortenedUrl() {
		// ARRANGE
		Url url = new Url("aBc123", "https://google.com");
		
		// ACT
		repository.save(url);
		Optional<Url> result = repository.findByShortenedUrl(url.getShortenedUrl());
		
		// ASSERT
		assertTrue(result.isPresent());
		assertEquals(url.getShortenedUrl(), result.get().getShortenedUrl());
		assertEquals(url.getOriginalUrl(), result.get().getOriginalUrl());
	}
	
	@Test
	@DisplayName("Deve retornar Optional vazio quando a chave fornecida não retorna nenhuma entidade Url existente")
	void shouldReturnEmpty_whenUrlDoesNotExistByGivenKey() {
		// ARRANGE
		Url nonExistentUrl = new Url("nonExistentUrl", "https://google.com");
		
		// ACT
		Optional<Url> result = repository.findByShortenedUrl(nonExistentUrl.getShortenedUrl()); // sem repository.save, deve retornar Optional.empty
		
		// ASSERT
		assertFalse(result.isPresent());
	}
	
	@Test
	@DisplayName("Deve persistir corretamente todos os campos da entidade Url fornecida")
	void shouldPersistAllFieldsOfGivenUrl() {
		// ARRANGE
		Url url = new Url("aBc123", "https://google.com");
		
		// ACT
		Url savedUrl = repository.save(url);
		
		// ASSERT
		assertNotNull(savedUrl);
		assertEquals(url.getShortenedUrl(), savedUrl.getShortenedUrl());
		assertEquals(url.getOriginalUrl(), savedUrl.getOriginalUrl());
	}
}
