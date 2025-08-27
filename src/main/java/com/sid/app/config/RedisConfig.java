package com.sid.app.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * Configuration class for Redis integration.
 * Defines beans for Redis connection factory and Redis template.
 * Uses Lettuce as the Redis client and configures serializers for keys and values.
 *
 * @author Siddhant Patni
 */
@Configuration
public class RedisConfig {

    /**
     * Creates a RedisConnectionFactory using Lettuce.
     * This factory manages connections to the Redis server.
     *
     * @return a LettuceConnectionFactory instance
     */
    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        return new LettuceConnectionFactory();
    }

    /**
     * Configures a RedisTemplate for performing Redis operations.
     * Sets up key and value serializers for storing data as JSON.
     *
     * @return a configured RedisTemplate instance
     */
    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory());
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        return template;
    }
}