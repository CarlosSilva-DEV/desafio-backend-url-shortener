package com.carlossilvadev.desafio_backend_url_shortener.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import com.carlossilvadev.desafio_backend_url_shortener.dto.UrlRequestDTO;
import com.carlossilvadev.desafio_backend_url_shortener.dto.UrlResponseDTO;
import com.carlossilvadev.desafio_backend_url_shortener.exceptions.UrlNotFoundException;
import com.carlossilvadev.desafio_backend_url_shortener.model.Url;
import com.carlossilvadev.desafio_backend_url_shortener.repository.UrlRepository;
import com.carlossilvadev.desafio_backend_url_shortener.service.utils.ShortenerConstants;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class) // habilita o Mockito sem subir contexto Spring
public class UrlShortenerServiceTest {
	
	@Mock // cria uma implementação simulada, usada em dependências da classe testada
	private UrlRepository repository;
	
	@InjectMocks // cria uma instancia real, usada na classe a ser testada
	private UrlShortenerService service;
	
	@Test
	@DisplayName("Deve encurtar URL fornecida e retornar DTO quando não houver conflito de chaves")
	void shouldSuccessShortenUrl_whenNoKeyConflictExists() {
		// ARRANGE
		String originalUrl = "https://google.com";
		UrlRequestDTO request = new UrlRequestDTO(originalUrl);
		
		// type-checking
		ArgumentCaptor<String> keyCaptor = ArgumentCaptor.forClass(String.class);
		ArgumentCaptor<Url> urlCaptor = ArgumentCaptor.forClass(Url.class);
		
		// simula que não haverá conflito de chaves
		when(repository.findByShortenedUrl(anyString())).thenReturn(Optional.empty());
		
		// simula salvamento de entidade Url com retorno do objeto salvo
		when(repository.save(any(Url.class))).thenAnswer(Invocation -> Invocation.getArgument(0));
		
		// ACT
		UrlResponseDTO response = service.shortenUrl(request);
		
		// ASSERT
		assertNotNull(response);
		assertNotNull(response.url());
		int length = response.url().length();
		
		// verifica tamanho da URL curta (entre 5-10 caracteres)
		assertTrue(length >= ShortenerConstants.MIN_LENGTH && length <= ShortenerConstants.MAX_LENGTH,
				"Comprimento esperado: entre %d e %d, mas foi: %d"
					.formatted(ShortenerConstants.MIN_LENGTH, ShortenerConstants.MAX_LENGTH, length)
		);
		
		// verifica se URL possui apenas caracteres alfanuméricos
		assertTrue(
				response.url().chars().allMatch(c -> ShortenerConstants.CHAR_POOL.indexOf(c) >= 0),
				"A URL encurtada contém caracteres fora do CHAR_POOL"
		);
		
		// verifica se o repository foi chamado corretamente
		verify(repository).findByShortenedUrl(keyCaptor.capture());
		verify(repository).save(urlCaptor.capture());
		
		String capturedKey = keyCaptor.getValue();
		Url capturedUrl = urlCaptor.getValue();
		
		assertEquals(capturedKey, capturedUrl.getShortenedUrl()); // verifica chave retornada é igual a shortenedUrl
		assertEquals(originalUrl, capturedUrl.getOriginalUrl()); // verifica se a URL original é igual a originalUrl retornada
		assertEquals(capturedKey, response.url()); // verifica se chave capturada é igual a URL curta gerada no DTO
	}
	
	@Test
	@DisplayName("Deve tentar gerar nova URL curta quando houver conflito de chaves")
	void shouldRetryShortenUrl_whenKeyAlreadyExists() {
		// ARRANGE
		UrlRequestDTO request = new UrlRequestDTO("https://google.com.br");
		Url conflictingUrl = new Url("randomUrl", "https://youtube.com");
		
		// simula que haverá conflito de chaves 1 vez, então shortenUrl() tentará gerar nova chave e resultará sucesso
		when(repository.findByShortenedUrl(anyString()))
				.thenReturn(Optional.of(conflictingUrl))
				.thenReturn(Optional.empty());
		
		when(repository.save(any(Url.class))).thenAnswer(Invocation -> Invocation.getArgument(0));
		
		// ACT
		UrlResponseDTO response = service.shortenUrl(request);
		
		// ASSERT
		assertNotNull(response);
		assertNotNull(response.url());
		
		// verifica número de chamadas (1 conflito + 1 sucesso)
		verify(repository, times(2)).findByShortenedUrl(anyString());
		verify(repository).save(any(Url.class));
	}
	
	@Test
	@DisplayName("Deve retornar DTO com URL original quando a chave buscada existir")
	void shouldSuccessFindOriginalUrl_whenKeyExists() {
		// ARRANGE
		String shortenedUrl = "aBc123";
		String originalUrl = "https://google.com";
		Url existingUrl = new Url(shortenedUrl, originalUrl);
		
		// simula recuperação de entidade Url com base na chave (shortenedUrl)
		when(repository.findByShortenedUrl(shortenedUrl)).thenReturn(Optional.of(existingUrl));
		
		// ACT
		UrlResponseDTO response = service.findOriginalUrl(shortenedUrl);
		
		// ASSERT
		assertNotNull(response);
		assertEquals(originalUrl, response.url());
		
		verify(repository).findByShortenedUrl(shortenedUrl);
	}
	
	@Test
	@DisplayName("Deve lançar UrlNotFoundException quando a chave buscada não existir")
	void shouldThrowExceptionFindOriginalUrl_whenKeyNotExists() {
		// ARRANGE
		String nonExistentUrl = "testUrl";
		
		// simula a busca por uma chave inexistente,retornando empty
		when(repository.findByShortenedUrl(nonExistentUrl)).thenReturn(Optional.empty());
		
		// ACT e ASSERT
		UrlNotFoundException exception = assertThrows( // verifica a exceção lançada pelo método
				UrlNotFoundException.class,
				() -> service.findOriginalUrl(nonExistentUrl)
		);
		
		// compara as mensagens das exceções lançadas
		assertEquals("A URL informada não existe: " + nonExistentUrl, exception.getMessage());
		
		verify(repository).findByShortenedUrl(nonExistentUrl);
	}
}