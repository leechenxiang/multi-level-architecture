package com.itzixi.test;
    
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.junit.Test;

public class CaffeineCacheTest {
    @Test
    public void testCache() {
        Cache<String, Object> cache = Caffeine.newBuilder().build();

        cache.put("username", "风间影月");
        cache.put("age", 28);
        cache.put("sex", "man");

        String username = cache.getIfPresent("username").toString();
        Integer age = Integer.valueOf(cache.getIfPresent("age").toString());
        String sex = cache.getIfPresent("sex").toString();

        System.out.println("username = " + username);
        System.out.println("age = " + age);
        System.out.println("sex = " + sex);
    }

    @Test
    public void testCacheSetDefaultValue() {
        Cache<String, Object> cache = Caffeine.newBuilder().build();

        String birthday1 = cache.get("birthday", s -> {
            return "2025-12-25";
        }).toString();
        System.out.println("birthday1 = " + birthday1);

        String birthday2 = cache.getIfPresent("birthday").toString();
        System.out.println("birthday2 = " + birthday2);
    }
}
