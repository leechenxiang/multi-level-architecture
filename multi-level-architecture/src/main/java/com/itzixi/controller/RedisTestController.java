package com.itzixi.controller;

import com.itzixi.utils.RedisOperator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("redis")
public class RedisTestController {

    @Autowired
    private RedisOperator redisOperator;

    @GetMapping("set")
    public Object setKeyValue(String key, String value) {
        redisOperator.set(key, value);
        return "redis - setKeyValue 操作成功";
    }

    @GetMapping("get")
    public Object getKeyValue(String key) {
        return redisOperator.get(key);
    }

    @GetMapping("delete")
    public Object deleteKeyValue(String key) {
        redisOperator.del(key);
        return "redis - deleteKeyValue 操作成功";
    }

    //@Autowired
    //private RedisTemplate redisTemplate;

    //@GetMapping("set")
    //public Object setKeyValue(String key, String value) {
    //    redisTemplate.opsForValue().set(key, value);
    //    return "redis - setKeyValue 操作成功";
    //}
    //
    //@GetMapping("get")
    //public Object getKeyValue(String key) {
    //    return redisTemplate.opsForValue().get(key);
    //}
    //
    //@GetMapping("delete")
    //public Object deleteKeyValue(String key) {
    //    redisTemplate.delete(key);
    //    return "redis - deleteKeyValue 操作成功";
    //}
}
