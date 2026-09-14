package com.carlossilvadev.desafio_backend_url_shortener.exceptions;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import jakarta.servlet.http.HttpServletRequest;

@ActiveProfiles("test")
public class GlobalExceptionHandlerTest {
    private GlobalExceptionHandler handler; // usado para criar uma instancia do handler
    private HttpServletRequest request; // usado para simular a requisição interceptada pelo handler

    private static final String URI = "/shorten-url"; // usado para simular o caminho das requisições POST

    @BeforeEach
    void setup() {
        handler = new GlobalExceptionHandler(); // instanciado diretamente, visto que não age como um controller tradicional, não é necessário um MockMvc para estes testes 
        request = mock(HttpServletRequest.class); // cria um mock da requisição
        when(request.getRequestURI()).thenReturn(URI); // define o caminho da requisição mock 
    }

    @Test
    @DisplayName("Deve retornar status 400 e StandardError com todos os FieldErrors quando estiver tratando uma MethodArgumentNotValidException")
    void shouldReturn400AndStandardErrorWithAllFieldErrors_whenHandlingMethodArgumentNotValidException() {
        // ARRANGE
        FieldError firstError = new FieldError("urlRequestDTO", "url", "Campo não pode ser vazio"); // cria um mock de FieldError a ser capturado na exceção
        FieldError secondError = new FieldError("urlRequestDTO", "url", "Campo deve ser preenchido com uma URL válida"); // cria um mock de FieldError a ser capturado na exceção
        
        BindingResult bindingResult = mock(BindingResult.class); // cria um mock de BindingResult que será injetado na exceção
        when(bindingResult.getFieldErrors()).thenReturn(List.of(firstError, secondError)); // liga os FieldErrors mockados com o BindingResult mockado

        MethodArgumentNotValidException exception = mock(MethodArgumentNotValidException.class); // cria um mock da exceção a ser tratada 
        when(exception.getBindingResult()).thenReturn(bindingResult); // liga o BindingResult mockado com a exceção mockada 

        final Integer EXPECTED_STATUS = HttpStatus.BAD_REQUEST.value();
        final String EXPECTED_ERROR = "Invalid input data";
        final String EXPECTED_MESSAGE = "url: Campo não pode ser vazio; url: Campo deve ser preenchido com uma URL válida";
       
        // ACT
        ResponseEntity<StandardError> response = handler.handleValidationException(exception, request);

        // ASSERT 

        // verifica se o status da resposta é igual ao esperado e se os campos do StandardError são iguais ao esperado
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST); 
        assertThat(response.getBody()).isNotNull(); 
        assertThat(response.getBody().getTimestamp()).isNotNull(); 
        assertThat(response.getBody().getStatus()).isEqualTo(EXPECTED_STATUS);
        assertThat(response.getBody().getError()).isEqualTo(EXPECTED_ERROR);
        assertThat(response.getBody().getMessage()).isEqualTo(EXPECTED_MESSAGE);
        assertThat(response.getBody().getPath()).isEqualTo(URI);
    } 
}
