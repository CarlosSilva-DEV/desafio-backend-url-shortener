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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@Tag(name = "UrlShortenerController", description = "Controller responsável pelas requisições de encurtamento e redirecionamento de URLs")
public class UrlShortenerController {
	private final UrlShortenerService service;
	
	public UrlShortenerController(UrlShortenerService service) {
		this.service = service;
	}
	
	@PostMapping("/shorten-url")
	@Operation(summary = "Gera uma URL curta", description = "Método responsável por receber uma URL longa e gerar uma URL curta entre 5-10 caracteres")
	@ApiResponse(responseCode = "200", description = "URL curta gerada com sucesso")
	@ApiResponse(responseCode = "400", description = "Erro ao tentar gerar URL curta por problemas na requisição (erros de validação, ausência de corpo da requisição ou JSON malformado)")
	@ApiResponse(responseCode = "500", description = "Erro interno no servidor")
	public ResponseEntity<UrlResponseDTO> shortenUrl(@RequestBody @Valid UrlRequestDTO request, HttpServletRequest servletRequest) {
		var originalUrl = service.shortenUrl(request).url(); // encurta URL e armazena o campo do DTO
		
		var redirectUrl = servletRequest.getRequestURL().toString().replace("shorten-url", originalUrl); // constrói a URL com host e porta + URL curta no caminho URI
		
		return ResponseEntity.status(HttpStatus.OK).body(new UrlResponseDTO(redirectUrl));
	}
	
	@GetMapping("/{request}")
	@Operation(summary = "Redireciona para a URL original", description = "Método responsável por receber uma URL curta e redirecionar para a página do endereço URL original")
	@ApiResponse(responseCode = "302", description = "URL original encontrada e usuário redirecionado com sucesso")
	@ApiResponse(responseCode = "404", description = "URL original não pôde ser encontrada ou não existe no banco de dados")
	@ApiResponse(responseCode = "500", description = "Erro interno no servidor")
	public ResponseEntity<Void> redirect(@PathVariable String request) {
		var originalUrl = service.findOriginalUrl(request).url();
		
		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(URI.create(originalUrl));
		
		return ResponseEntity.status(HttpStatus.FOUND).headers(headers).build();
	}
}