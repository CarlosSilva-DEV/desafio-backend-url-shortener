package com.carlossilvadev.desafio_backend_url_shortener.exceptions;

public class UrlShorteningException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	public UrlShorteningException(String msg) {
		super(msg);
	}
}