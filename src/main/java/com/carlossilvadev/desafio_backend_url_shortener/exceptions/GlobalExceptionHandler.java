package com.carlossilvadev.desafio_backend_url_shortener.exceptions;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestControllerAdvice
public class GlobalExceptionHandler {
	private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);
	
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
	
	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<StandardError> handleHttpMessageNotReadable(HttpMessageNotReadableException exception, HttpServletRequest request) {
		String error = "HTTP message is not readable";
		HttpStatus status = HttpStatus.BAD_REQUEST;
		String message = "Request body is required but was not provided";
		
		if (!exception.getMessage().contains("Required request body is missing")) {
			message = exception.getMessage();
		}
		
		StandardError handledException = new StandardError(Instant.now(), status.value(), error, message, request.getRequestURI());		
		return ResponseEntity.status(status).body(handledException);
	}
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<StandardError> handleUnexpectedExceptions(Exception exception, HttpServletRequest request) {
		// log de erro slf4j
		log.error("Internal Server Error: ", exception);
				
		String error = "Internal Server Error";
		HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
		StandardError handledException = new StandardError(Instant.now(), status.value(), error, "Ocorreu um erro interno inesperado do servidor", request.getRequestURI());
		return ResponseEntity.status(status).body(handledException);
	}
}
