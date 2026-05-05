package com.carlossilvadev.desafio_backend_url_shortener.model;

import java.util.Objects;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@RedisHash(value = "urls", timeToLive = 180L) // define a classe como uma entidade Hash, define namespace "urls" e TTL
public class Url {
	@Id // nesse contexto, define esse campo como a chave
	private String shortenedUrl;
	private String originalUrl;
	
	// Construtores
	public Url(String shortenedUrl, String originalUrl) {
		this.shortenedUrl = shortenedUrl;
		this.originalUrl = originalUrl;
	}
	
	public Url() {
	}
	
	// getters e setters
	public String getShortenedUrl() {
		return shortenedUrl;
	}
	public void setShortenedUrl(String shortenedUrl) {
		this.shortenedUrl = shortenedUrl;
	}

	public String getOriginalUrl() {
		return originalUrl;
	}
	public void setOriginalUrl(String originalUrl) {
		this.originalUrl = originalUrl;
	}
	
	// hashcode e equals
	@Override
	public int hashCode() {
		return Objects.hash(shortenedUrl);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Url other = (Url) obj;
		return Objects.equals(shortenedUrl, other.shortenedUrl);
	}
}
