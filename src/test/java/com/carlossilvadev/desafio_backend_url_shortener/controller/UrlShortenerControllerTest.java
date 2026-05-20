package com.carlossilvadev.desafio_backend_url_shortener.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

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
}
