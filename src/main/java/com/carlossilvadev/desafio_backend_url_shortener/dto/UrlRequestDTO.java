package com.carlossilvadev.desafio_backend_url_shortener.dto;

import org.hibernate.validator.constraints.URL;

import jakarta.validation.constraints.NotBlank;

public record UrlRequestDTO(
		@NotBlank(message = "Campo não pode ser vazio")
		@URL(protocol = "https", message = "Campo deve ser preenchido com uma URL válida")
		String url) {
}