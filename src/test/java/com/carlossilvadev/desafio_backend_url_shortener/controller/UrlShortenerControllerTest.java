package com.carlossilvadev.desafio_backend_url_shortener.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.MethodArgumentNotValidException;

import com.carlossilvadev.desafio_backend_url_shortener.dto.UrlRequestDTO;
import com.carlossilvadev.desafio_backend_url_shortener.dto.UrlResponseDTO;
import com.carlossilvadev.desafio_backend_url_shortener.service.UrlShortenerService;

import tools.jackson.databind.ObjectMapper;

@ActiveProfiles("test")
@WebMvcTest(UrlShortenerController.class)
public class UrlShortenerControllerTest {
	@Autowired
	private MockMvc mockMvc; // mock de cliente HTTP
	
	@Autowired
	private ObjectMapper mapper; // mapear objetos Java para JSON
	
	@MockitoBean
	private UrlShortenerService service; // mock do serviço
	
	// POST /shorten-url
	@Test
	@DisplayName("Deve retornar status 201 e UrlResponseDTO com URL curta caso o UrlRequestDTO fornecido seja válido")
	void shouldReturn201AndSuccessShortenUrl_whenGivenDtoIsValid() throws Exception {
		// ARRANGE
		UrlRequestDTO request = new UrlRequestDTO("https://google.com");
		final String URI = "/shorten-url";
		
		when(service.shortenUrl(any(UrlRequestDTO.class))).thenReturn(new UrlResponseDTO("aBc123"));
		
		// ACT & ASSERT
		mockMvc.perform(post(URI) // simula uma requisição HTTP
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(request))) // mapeia UrlRequestDTO para JSON (String)
		.andExpect(status().isCreated()) // verifica status retornado
		.andExpect(jsonPath("$.url").value("http://localhost/aBc123")); // equivalente a criar redirectUrl
		
		verify(service).shortenUrl(any(UrlRequestDTO.class));
	}
	
	@Test
	@DisplayName("Deve retornar status 400 e lançar MethodArgumentNotValidException caso o campo url do UrlRequestDTO fornecido seja vazio")
	void shouldReturn400AndThrowMethodArgumentNotValidException_whenDtoUrlFieldIsBlank() throws Exception {
		// ARRANGE
		UrlRequestDTO request = new UrlRequestDTO("");
		final String URI = "/shorten-url";
		final Integer EXPECTED_STATUS = HttpStatus.BAD_REQUEST.value();
		final String EXPECTED_ERROR = "Invalid input data";
		final String EXPECTED_MESSAGE = "url: Campo não pode ser vazio";
		
		// ACT & ASSERT
		mockMvc.perform(post(URI)
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(request)))
		.andExpect(result -> {
			Exception exception = result.getResolvedException(); // extraindo exceção lançada
			assertThat(exception).isInstanceOf(MethodArgumentNotValidException.class); // comparando com exceção esperada
		})
		// verifica formato esperado do StandardError retornado no JSON
		.andExpect(jsonPath("$.timestamp").exists())
		.andExpect(jsonPath("$.status").value(EXPECTED_STATUS))
		.andExpect(jsonPath("$.error").value(EXPECTED_ERROR))
		.andExpect(jsonPath("$.message").value(EXPECTED_MESSAGE))
		.andExpect(jsonPath("$.path").value(URI));
		
		// validação falhou, então não deve ocorrer interações com UrlShortenerService
		verifyNoInteractions(service);
	}
	
	@Test
	@DisplayName("Deve retornar status 400 e lançar MethodArgumentNotValidException caso a url fornecida em UrlRequestDTO não contenha protocolo HTTPS")
	void shouldReturn400AndThrowMethodArgumentNotValidException_whenDtoUrlFieldIsNotHttps() throws Exception {
		// ARRANGE
		UrlRequestDTO request = new UrlRequestDTO("http://google.com"); // URL inválida (protocolo HTTP em vez de HTTPS)
		final String URI = "/shorten-url";
		final Integer EXPECTED_STATUS = HttpStatus.BAD_REQUEST.value();
		final String EXPECTED_ERROR = "Invalid input data";
		final String EXPECTED_MESSAGE = "url: Campo deve ser preenchido com uma URL válida";
				
		// ACT & ASSERT
		mockMvc.perform(post(URI)
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(request)))
		.andExpect(result -> {
			Exception exception = result.getResolvedException();
			assertThat(exception).isInstanceOf(MethodArgumentNotValidException.class);
		})
		.andExpect(jsonPath("$.timestamp").exists())
		.andExpect(jsonPath("$.status").value(EXPECTED_STATUS))
		.andExpect(jsonPath("$.error").value(EXPECTED_ERROR))
		.andExpect(jsonPath("$.message").value(EXPECTED_MESSAGE))
		.andExpect(jsonPath("$.path").value(URI));
		
		verifyNoInteractions(service);
	}
}
