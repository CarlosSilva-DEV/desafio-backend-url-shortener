package com.carlossilvadev.desafio_backend_url_shortener.service;

import org.springframework.stereotype.Service;

import com.carlossilvadev.desafio_backend_url_shortener.dto.UrlRequestDTO;
import com.carlossilvadev.desafio_backend_url_shortener.dto.UrlResponseDTO;
import com.carlossilvadev.desafio_backend_url_shortener.exceptions.UrlNotFoundException;
import com.carlossilvadev.desafio_backend_url_shortener.exceptions.UrlShorteningException;
import com.carlossilvadev.desafio_backend_url_shortener.model.Url;
import com.carlossilvadev.desafio_backend_url_shortener.repository.UrlRepository;
import com.carlossilvadev.desafio_backend_url_shortener.service.utils.ShortenerConstants;

@Service
public class UrlShortenerService {
	private final UrlRepository repository;
	
	public UrlShortenerService(UrlRepository repository) {
		this.repository = repository;
	}
	
	public UrlResponseDTO shortenUrl(UrlRequestDTO request) {
		while (true) {
			String shortenUrl = generateRandomKey( // gera uma String aleatória entre 5-10 caracteres
					ShortenerConstants.MIN_LENGTH + ShortenerConstants.SECURE_RANDOM
					.nextInt(ShortenerConstants.MAX_LENGTH - ShortenerConstants.MIN_LENGTH + 1));
			
			Url url = new Url(shortenUrl, request.url());
				
			if (repository.findByShortenedUrl(shortenUrl).isPresent()) { // verifica no Redis se existe uma chave como a gerada. caso exista, lança exceção
				throw new UrlShorteningException("A URL curta gerada já existe: " + shortenUrl);
			}
			
			return new UrlResponseDTO(repository.save(url).getShortenedUrl());
		}
	}
	
	public UrlResponseDTO findOriginalUrl(String request) {
		String originalUrl = repository.findByShortenedUrl(request)
				.map(Url::getOriginalUrl).orElseThrow(() -> new UrlNotFoundException("A URL informada não existe: " + request));
		return new UrlResponseDTO(originalUrl);
	}
	
	// método auxiliar (shortenUrl)
	private String generateRandomKey(Integer length) {
		StringBuilder sb = new StringBuilder(length);
		for (int i = 0; i < length; i++) {
			sb.append(ShortenerConstants.CHAR_POOL.charAt(ShortenerConstants.SECURE_RANDOM.nextInt(ShortenerConstants.CHAR_POOL.length())));
		}
		return sb.toString();
	}
}