package com.carlossilvadev.desafio_backend_url_shortener.exceptions;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


import org.junit.jupiter.api.BeforeEach;
import org.springframework.test.context.ActiveProfiles;

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
}
