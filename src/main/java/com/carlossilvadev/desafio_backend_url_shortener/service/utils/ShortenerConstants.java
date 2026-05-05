package com.carlossilvadev.desafio_backend_url_shortener.service.utils;

import java.security.SecureRandom;

public class ShortenerConstants {
	public static final String CHAR_POOL = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
	public static final SecureRandom SECURE_RANDOM = new SecureRandom();
	public static final Integer MIN_LENGTH = 5;
	public static final Integer MAX_LENGTH = 10;
	
	private ShortenerConstants() {
	}
}