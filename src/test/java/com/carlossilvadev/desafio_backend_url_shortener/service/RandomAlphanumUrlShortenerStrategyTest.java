package com.carlossilvadev.desafio_backend_url_shortener.service;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

import com.carlossilvadev.desafio_backend_url_shortener.service.generator.RandomAlphanumUrlShortenerStrategy;

@ActiveProfiles("test")
public class RandomAlphanumUrlShortenerStrategyTest {
    private final RandomAlphanumUrlShortenerStrategy shortener = new RandomAlphanumUrlShortenerStrategy(); 

    @Test
    @DisplayName("Deve gerar uma chave aleatória com tamanho entre 5 e 10 caracteres")
    void shouldGenerateRandomKey_withinExpectedLength() {
        String key = shortener.generateRandomKey(); // gera a chave aleatória a ser incluída na URL curta
        assertTrue(key.length() >= shortener.getMinLength() && key.length() <= shortener.getMaxLength()); // verifica se o tamanho está dentro do esperado
    }

    @Test
    @DisplayName("Deve gerar uma chave aleatória apenas com caracteres alfanuméricos")
    void shouldGenerateRandomKey_withOnlyAlphanumericCharacters() {
        String key = shortener.generateRandomKey(); // gera a chave aleatória a ser incluída na URL curta
        assertTrue(key.chars().allMatch(Character::isLetterOrDigit)); // verifica se cada caracter é uma letra ou número
    }
}
