package com.carlossilvadev.desafio_backend_url_shortener.controller;

import java.net.URI;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.carlossilvadev.desafio_backend_url_shortener.dto.UrlRequestDTO;
import com.carlossilvadev.desafio_backend_url_shortener.dto.UrlResponseDTO;
import com.carlossilvadev.desafio_backend_url_shortener.service.UrlShortenerService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
public class UrlShortenerController {
	private final UrlShortenerService service;
	
	public UrlShortenerController(UrlShortenerService service) {
		this.service = service;
	}
	
	@PostMapping("/shorten-url")
	public ResponseEntity<UrlResponseDTO> shortenUrl(@RequestBody @Valid UrlRequestDTO request, HttpServletRequest servletRequest) {
		var originalUrl = service.shortenUrl(request).url(); // encurta URL e armazena o campo do DTO
		
		var redirectUrl = servletRequest.getRequestURL().toString().replace("shorten-url", originalUrl); // constrói a URL com host e porta + URL curta no caminho URI
		
		return ResponseEntity.status(HttpStatus.CREATED).body(new UrlResponseDTO(redirectUrl));
	}
	
	@GetMapping("/{request}")
	public ResponseEntity<Void> redirect(@PathVariable String request) {
		var originalUrl = service.findOriginalUrl(request).url();
		
		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(URI.create(originalUrl));
		
		return ResponseEntity.status(HttpStatus.FOUND).headers(headers).build();
	}
}