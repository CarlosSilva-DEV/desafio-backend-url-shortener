package com.carlossilvadev.desafio_backend_url_shortener.service;

import org.springframework.stereotype.Service;

import com.carlossilvadev.desafio_backend_url_shortener.dto.UrlRequestDTO;
import com.carlossilvadev.desafio_backend_url_shortener.dto.UrlResponseDTO;
import com.carlossilvadev.desafio_backend_url_shortener.exceptions.UrlNotFoundException;
import com.carlossilvadev.desafio_backend_url_shortener.model.Url;
import com.carlossilvadev.desafio_backend_url_shortener.repository.UrlRepository;
import com.carlossilvadev.desafio_backend_url_shortener.service.generator.UrlShortenerStrategy;

@Service
public class UrlShortenerService {
	private final UrlRepository repository;
    private final UrlShortenerStrategy shortener;
	
	public UrlShortenerService(UrlRepository repository, UrlShortenerStrategy shortener) {
		this.repository = repository;
        this.shortener = shortener;
	}
	
	public UrlResponseDTO shortenUrl(UrlRequestDTO request) {
		while (true) {
			String shortenUrl = shortener.generateRandomKey(); // gera uma String aleatória entre 5-10 caracteres				
			if (repository.findByShortenedUrl(shortenUrl).isEmpty()) { // verifica no Redis se existe uma chave como a gerada. caso não exista, cria a entidade e salva no Redis
				Url url = new Url(shortenUrl, request.url());
				return new UrlResponseDTO(repository.save(url).getShortenedUrl());
			}
			// caso a chave já exista no Redis, continua em uma nova iteração para tentar criar outra chave
		}
	}
	
	public UrlResponseDTO findOriginalUrl(String request) {
		String originalUrl = repository.findByShortenedUrl(request)
				.map(Url::getOriginalUrl).orElseThrow(() -> new UrlNotFoundException("A URL informada não existe: " + request));
		return new UrlResponseDTO(originalUrl);
	}
}
