package com.carlossilvadev.desafio_backend_url_shortener.exceptions;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<StandardError> handleValidationException(MethodArgumentNotValidException exception, HttpServletRequest request) {
		List<String> errors = exception.getBindingResult()
				.getFieldErrors()
				.stream()
				.map(error -> error.getField() + ": " + error.getDefaultMessage())
				.collect(Collectors.toList());
		
		String errorMessage = String.join("; ", errors);
		HttpStatus status = HttpStatus.BAD_REQUEST;
		StandardError handledException = new StandardError(Instant.now(), status.value(), "Invalid input data", errorMessage, request.getRequestURI());
		return ResponseEntity.status(status).body(handledException);
	}
	
	@ExceptionHandler(UrlNotFoundException.class)
	public ResponseEntity<StandardError> handleUrlNotFound(UrlNotFoundException exception, HttpServletRequest request) {
		String error = "URL not found";
		HttpStatus status = HttpStatus.NOT_FOUND;
		StandardError handledException = new StandardError(Instant.now(), status.value(), error, exception.getMessage(), request.getRequestURI());		
		return ResponseEntity.status(status).body(handledException);
	}
	
	@ExceptionHandler(UrlShorteningException.class)
	public ResponseEntity<StandardError> handleExistingUrlException(UrlShorteningException exception, HttpServletRequest request) {
		String error = "The given URL already exists";
		HttpStatus status = HttpStatus.CONFLICT;
		StandardError handledException = new StandardError(Instant.now(), status.value(), error, exception.getMessage(), request.getRequestURI());		
		return ResponseEntity.status(status).body(handledException);
	}
}
