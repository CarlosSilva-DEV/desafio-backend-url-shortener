package com.carlossilvadev.desafio_backend_url_shortener.exceptions;

import java.time.Instant;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {
	@ExceptionHandler(UrlShorteningException.class)
	public ResponseEntity<StandardError> handleExistingUrlException(UrlShorteningException exception, HttpServletRequest request) {
		String error = "The given URL already exists";
		HttpStatus status = HttpStatus.CONFLICT;
		StandardError handledException = new StandardError(Instant.now(), status.value(), error, exception.getMessage(), request.getRequestURI());		
		return ResponseEntity.status(status).body(handledException);
	}
}
