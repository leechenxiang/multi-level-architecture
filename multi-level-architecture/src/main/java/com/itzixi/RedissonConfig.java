package com.itzixi;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedissonConfig {

    @Value("${spring.redis.host}")
    public String redisHost;

    @Bean(destroyMethod = "shutdown")
    public RedissonClient redissonClient() {
        Config config = new Config();
        config.useSingleServer()
                .setAddress("redis://" + redisHost + ":6379")   // redis server 所在地址（单节点）
//                .setUsername("test")                 // 设置用户名
                .setPassword("123456")                   // 设置密码
                .setDatabase(0)                         // 指定16个库中可以设置某个下标的库
                .setConnectionMinimumIdleSize(10)       // 设置最小空闲连接数
                .setConnectionPoolSize(20)              // 连接池最大连接数
                .setIdleConnectionTimeout(60 * 1000)    // 销毁超时连接数
                .setConnectTimeout(15 * 1000)           // 客户端获得redis连接的超时时间
                .setTimeout(15 * 1000)                  // 响应的超时时间
        ;
        return Redisson.create(config);
    }

}
