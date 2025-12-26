package com.puspo.scalablekafkaapp.videoshare.config.cache;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.github.benmanes.caffeine.cache.Caffeine;

import java.time.Duration;

@Configuration
@EnableCaching
public class CacheConfig {

    @Bean
    public CacheManager cacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager(); // this is the cache manager for the cache
        cacheManager.setCaffeine(
                Caffeine.newBuilder()
                        .maximumSize(1000) // this is the maximum size for the cache
                        .expireAfterWrite(Duration.ofHours(1)) // this is the time to live for the cache
                        .recordStats());
        return cacheManager;
    }
}
