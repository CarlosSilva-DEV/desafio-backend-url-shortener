package com.carlossilvadev.desafio_backend_url_shortener.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.MethodArgumentNotValidException;

import com.carlossilvadev.desafio_backend_url_shortener.dto.UrlRequestDTO;
import com.carlossilvadev.desafio_backend_url_shortener.dto.UrlResponseDTO;
import com.carlossilvadev.desafio_backend_url_shortener.exceptions.UrlNotFoundException;
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
	@DisplayName("Deve retornar status 200 e UrlResponseDTO com URL curta caso o UrlRequestDTO fornecido seja válido")
	void shouldReturn200AndSuccessShortenUrl_whenGivenDtoIsValid() throws Exception {
		// ARRANGE
		UrlRequestDTO request = new UrlRequestDTO("https://google.com");
		final String URI = "/shorten-url";
		
		when(service.shortenUrl(any(UrlRequestDTO.class))).thenReturn(new UrlResponseDTO("aBc123"));
		
		// ACT & ASSERT
		mockMvc.perform(post(URI) // simula uma requisição HTTP
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(request))) // mapeia UrlRequestDTO para JSON (String)
		.andExpect(status().isOk()) // verifica status retornado
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
	
	@Test
	@DisplayName("Deve retornar status 400 e lançar HttpMessageNotReadableException caso uma requisição sem corpo seja enviada")
	void shouldReturn400AndThrowHttpMessageNotReadableException_whenRequestBodyIsMissing() throws Exception {
		// ARRANGE
		final String URI = "/shorten-url";
		final Integer EXPECTED_STATUS = HttpStatus.BAD_REQUEST.value();
		final String EXPECTED_ERROR = "HTTP message is not readable";
		final String EXPECTED_MESSAGE = "Request body is required but was not provided";
		
		// ACT & ASSERT
		mockMvc.perform(post(URI) // requisição POST sendo enviada sem body, deve lançar HttpMessageNotReadableException
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(result -> {
			Exception exception = result.getResolvedException();
			assertThat(exception).isInstanceOf(HttpMessageNotReadableException.class);
		})
		.andExpect(jsonPath("$.timestamp").exists())
		.andExpect(jsonPath("$.status").value(EXPECTED_STATUS))
		.andExpect(jsonPath("$.error").value(EXPECTED_ERROR))
		.andExpect(jsonPath("$.message").value(EXPECTED_MESSAGE))
		.andExpect(jsonPath("$.path").value(URI));
		
		verifyNoInteractions(service);
	}
	
	@Test
	@DisplayName("Deve retornar status 400 e lançar HttpMessageNotReadableException caso uma requisição com JSON malformado seja enviada")
	void shouldReturn400AndThrowHttpMessageNotReadableException_whenRequestBodyHasMalformedJSON() throws Exception {
		// ARRANGE
		String malformedJson = "{url: \"https://google.com\"}";
		final String URI = "/shorten-url";
		final Integer EXPECTED_STATUS = HttpStatus.BAD_REQUEST.value();
		final String EXPECTED_ERROR = "HTTP message is not readable";
		final String EXPECTED_MESSAGE = "JSON parse error";
		
		// ACT & ASSERT
		mockMvc.perform(post(URI)
				.contentType(MediaType.APPLICATION_JSON)
				.content(malformedJson)) // requisição sendo enviada com JSON malformado, deve lançar HttpMessageNotReadableException
		.andExpect(result -> {
			Exception exception = result.getResolvedException();
			assertThat(exception).isInstanceOf(HttpMessageNotReadableException.class);
		})
		.andExpect(jsonPath("$.timestamp").exists())
		.andExpect(jsonPath("$.status").value(EXPECTED_STATUS))
		.andExpect(jsonPath("$.error").value(EXPECTED_ERROR))
		.andExpect(jsonPath("$.message").value(containsString(EXPECTED_MESSAGE)))
		.andExpect(jsonPath("$.path").value(URI));
		
		verifyNoInteractions(service);
	}
	
	// GET /{request}
	@Test
	@DisplayName("Deve retornar status 302 e redirecionar com sucesso caso a URL original esteja no header da resposta e o corpo da resposta seja vazio")
	void shouldReturn302AndSuccessRedirect_whenOriginalUrlIsFound() throws Exception {
		// ARRANGE
		String shortenedUrl = "aBc123";
		String originalUrl = "https://google.com";
		
		when(service.findOriginalUrl(shortenedUrl)).thenReturn(new UrlResponseDTO(originalUrl));
		
		// ACT & ASSERT
		mockMvc.perform(get("/{request}", shortenedUrl))
				.andExpect(status().isFound())
				.andExpect(header().string(HttpHeaders.LOCATION, originalUrl))
				.andExpect(content().string(""));
		
		verify(service).findOriginalUrl(shortenedUrl);
	}
	
	@Test
	@DisplayName("Deve retornar status 404 e lançar UrlNotFoundException caso a URL original não seja encontrada")
	void shouldReturn404AndThrowUrlNotFoundException_whenOriginalUrlIsNotFound() throws Exception {
		// ARRANGE
		String nonExistentUrl = "testUrl";
		final Integer EXPECTED_STATUS = HttpStatus.NOT_FOUND.value();
		final String EXPECTED_ERROR = "URL not found";
		final String EXPECTED_MESSAGE = "A URL informada não existe: " + nonExistentUrl;
		
		when(service.findOriginalUrl(nonExistentUrl)).thenThrow(new UrlNotFoundException(EXPECTED_MESSAGE));
		
		// ACT & ASSERT
		mockMvc.perform(get("/{request}", nonExistentUrl))
				.andExpect(result -> {
					Exception exception = result.getResolvedException();
					assertThat(exception).isInstanceOf(UrlNotFoundException.class);
				})
				.andExpect(jsonPath("$.timestamp").exists())
				.andExpect(jsonPath("$.status").value(EXPECTED_STATUS))
				.andExpect(jsonPath("$.error").value(EXPECTED_ERROR))
				.andExpect(jsonPath("$.message").value(EXPECTED_MESSAGE))
				.andExpect(jsonPath("$.path").value("/" + nonExistentUrl));
		
		verify(service).findOriginalUrl(nonExistentUrl);
	}
}
