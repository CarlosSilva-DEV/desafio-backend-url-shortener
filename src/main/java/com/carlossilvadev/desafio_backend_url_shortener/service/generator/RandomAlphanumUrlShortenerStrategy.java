package com.carlossilvadev.desafio_backend_url_shortener.service.generator;

import java.security.SecureRandom;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Component
@Primary // define esta classe como a implementação principal a ser usada pelo Spring (útil para quando existem várias implementações de uma mesma interface)
public class RandomAlphanumUrlShortenerStrategy implements UrlShortenerStrategy {
	private final String CHAR_POOL = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
	private final SecureRandom SECURE_RANDOM = new SecureRandom();
	private final int MIN_LENGTH = 5;
	private final int MAX_LENGTH = 10;
	
	public RandomAlphanumUrlShortenerStrategy() {
        // construtor vazio padrão
	}

	@Override
	public String generateRandomKey() {
        int length = MIN_LENGTH + SECURE_RANDOM.nextInt(MAX_LENGTH - MIN_LENGTH + 1);
		StringBuilder sb = new StringBuilder(length);
		for (int i = 0; i < length; i++) {
			sb.append(CHAR_POOL.charAt(SECURE_RANDOM.nextInt(CHAR_POOL.length())));
		}
		return sb.toString();
	}

    // getters apenas para uso na classe de teste unitário, respeitando encapsulamento
    public int getMinLength() {
        return MIN_LENGTH;
    } 

    public int getMaxLength() {
        return MAX_LENGTH;
    }
}
