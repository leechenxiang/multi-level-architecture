package com.itzixi.mq;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * RabbitMQ 的配置类
 */
@Configuration
public class RabbitMQConfig {

    // 定义交换机的名称
    public static final String EXCHANGE_SYNC_CACHE = "exchange_sync_cache";

    // 定义队列的名称
    public static final String QUEUE_SYNC_CACHE = "queue_sync_cache_8090";

    // 统一定义路由key
    public static final String ROUTING_KEY_SYNC_CACHE_INSERT = "sync.cache.insert";
    public static final String ROUTING_KEY_SYNC_CACHE_MODIFY = "sync.cache.modify";
    public static final String ROUTING_KEY_SYNC_CACHE_DELETE = "sync.cache.delete";

    /**
     * 创建交换机
     * @return
     */
    @Bean(EXCHANGE_SYNC_CACHE)
    public Exchange exchange() {
        return ExchangeBuilder
                    .fanoutExchange(EXCHANGE_SYNC_CACHE)
                    .durable(true)
                    .build();
    }

    /**
     * 创建队列
     * @return
     */
    @Bean(QUEUE_SYNC_CACHE)
    public Queue queue() {
        return QueueBuilder
                .durable(QUEUE_SYNC_CACHE)
                .build();
    }

    /**
     * 创建交换机与队列的绑定关系
     * @param exchange
     * @param queue
     * @return
     */
    @Bean
    public Binding bindingRelationship(@Qualifier(EXCHANGE_SYNC_CACHE) Exchange exchange,
                                       @Qualifier(QUEUE_SYNC_CACHE) Queue queue) {
        return BindingBuilder
                .bind(queue)
                .to(exchange)
                .with("sync.cache.*")
                .noargs();
    }

}
