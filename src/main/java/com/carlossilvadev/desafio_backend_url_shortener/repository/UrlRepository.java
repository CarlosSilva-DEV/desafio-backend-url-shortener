package com.carlossilvadev.desafio_backend_url_shortener.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.carlossilvadev.desafio_backend_url_shortener.model.Url;

@Repository
public interface UrlRepository extends CrudRepository<Url, String> {
	Optional<Url> findByShortenedUrl(String shortenedUrl);
}