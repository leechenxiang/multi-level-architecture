package com.itzixi;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.itzixi.pojo.ItemCategory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class CaffeineConfig {

    @Bean
    public Cache<String, String> myCache() {
        return Caffeine.newBuilder()
                .initialCapacity(10)
                .maximumSize(100)
                .build();
    }

    @Bean
    public Cache<String, ItemCategory> cache() {
        return Caffeine.newBuilder()
                .initialCapacity(10)
                .maximumSize(100)
                .build();
    }

    @Bean
    public Cache<String, List<ItemCategory>> categoryListCache() {
        return Caffeine.newBuilder()
                .initialCapacity(10)
                .maximumSize(100)
                .build();
    }

}
