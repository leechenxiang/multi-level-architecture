package com.itzixi.mq;

import com.github.benmanes.caffeine.cache.Cache;
import com.itzixi.pojo.ItemCategory;
import com.itzixi.service.ItemCategoryService;
import com.itzixi.utils.JsonUtils;
import com.itzixi.utils.RedisOperator;
import com.rabbitmq.client.Channel;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class RabbitMQConsumer {

    @Autowired
    private ItemCategoryService itemCategoryService;

    @Resource
    private Cache<String, ItemCategory> cache;

    @Autowired
    private RedisOperator redisOperator;

    @RabbitListener(queues = {RabbitMQConfig.QUEUE_SYNC_CACHE})
    public void watchQueue(Message message, Channel channel) throws Exception {

        String routingKey = message.getMessageProperties().getReceivedRoutingKey();
        System.out.println("routingKey = " + routingKey);

        String msg = new String(message.getBody());
        System.out.println("msg = " + msg);

        if (StringUtils.isBlank(msg)) {
            return;
        }

        String itemCategoryId = msg;
        ItemCategory itemCategoryPending = itemCategoryService.queryItemCategoryById(Integer.valueOf(itemCategoryId));

        String itemCategoryKey = "itemCategory:" + itemCategoryId;

        if (routingKey.equalsIgnoreCase(RabbitMQConfig.ROUTING_KEY_SYNC_CACHE_INSERT)) {
            System.out.println("此处执行数据新增操作的业务处理");

            // 覆盖本地缓存Caffeine中的老数据
            cache.put(itemCategoryKey, itemCategoryPending);

            // 覆盖分布式缓存Redis中的老数据
            redisOperator.set(itemCategoryKey, JsonUtils.objectToJson(itemCategoryPending));

        } else if (routingKey.equalsIgnoreCase(RabbitMQConfig.ROUTING_KEY_SYNC_CACHE_MODIFY)) {
            System.out.println("此处执行数据修改操作的业务处理");

            // 覆盖本地缓存Caffeine中的老数据
            cache.put(itemCategoryKey, itemCategoryPending);

            // 覆盖分布式缓存Redis中的老数据
            redisOperator.set(itemCategoryKey, JsonUtils.objectToJson(itemCategoryPending));

        } else if (routingKey.equalsIgnoreCase(RabbitMQConfig.ROUTING_KEY_SYNC_CACHE_DELETE)) {
            System.out.println("此处执行数据删除操作的业务处理");

            // 删除本地缓存Caffeine中的老数据
            cache.invalidate(itemCategoryKey);

            // 删除分布式缓存Redis中的老数据
            redisOperator.del(itemCategoryKey);
        }
    }
}
