package com.carlossilvadev.desafio_backend_url_shortener.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.JacksonJsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.carlossilvadev.desafio_backend_url_shortener.model.Url;

@Configuration
public class RedisConfig {
	@Bean // template específico para URL
	public RedisTemplate<String, Url> urlRedisTemplate(RedisConnectionFactory connectionFactory) {		
		
		// Serializer de chaves (String -> bytes)
		StringRedisSerializer stringSerializer = new StringRedisSerializer();
		
		// Serializer de valores: (Url -> JSON)
		JacksonJsonRedisSerializer<Url> jsonSerializer = new JacksonJsonRedisSerializer<>(Url.class);
		
		RedisTemplate<String, Url> template = new RedisTemplate<>();
		template.setConnectionFactory(connectionFactory);
		
		template.setKeySerializer(stringSerializer);
		template.setValueSerializer(jsonSerializer);
		template.setHashKeySerializer(stringSerializer);
		template.setHashValueSerializer(jsonSerializer);
		
		template.afterPropertiesSet();
		
		return template;
	}
}