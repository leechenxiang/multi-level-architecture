package com.itzixi.controller;

import com.github.benmanes.caffeine.cache.Cache;
import com.itzixi.mq.RabbitMQConfig;
import com.itzixi.pojo.ItemCategory;
import com.itzixi.service.ItemCategoryService;
import com.itzixi.utils.JsonUtils;
import com.itzixi.utils.RedisOperator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("itemCategory")
public class ItemCategoryController {

    @Autowired
    private ItemCategoryService itemCategoryService;

    @Resource
    private Cache<String, ItemCategory> cache;

    @Autowired
    private RedisOperator redisOperator;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 扩充为集群后，同时增加分布式缓存Redis
     * 分布式缓存Redis + 本地缓存Caffeine 双屏障为服务接口提供强大的缓存功能
     * @param id
     * @return
     */
    @GetMapping("get")
    public ItemCategory getItemCategory(Integer id) {

        String itemCategoryKey = "itemCategory:" + id;

        ItemCategory itemCategory = cache.get(itemCategoryKey, s -> {
            System.out.println("本地缓存中没有["+id+"]的值，现尝试从Redis中查询后再返回。");

            ItemCategory itemCategoryTemp = null;
            String itemCategoryJsonStr = redisOperator.get(itemCategoryKey);
            // 判断从redis中查询到的商品分类数据是否为空
            if (StringUtils.isBlank(itemCategoryJsonStr)) {
                System.out.println("Redis中不存在该数据，将从数据库中查询...");

                // 如果为空，则进入本条件，并从数据库中查询数据
                itemCategoryTemp = itemCategoryService.queryItemCategoryById(id);

                // 手动把商品分类数据设置到redis中，后续再次查询则redis中会有值
                String itemCategoryJson = JsonUtils.objectToJson(itemCategoryTemp);
                redisOperator.set(itemCategoryKey, itemCategoryJson);
            } else {
                System.out.println("Redis中存在该商品分类数据，此处则直接返回...");

                // 如果不为空，则直接转换json类型为ItemCategory后再返回即可
                itemCategoryTemp = JsonUtils.jsonToPojo(itemCategoryJsonStr, ItemCategory.class);
            }

            // 不论从redis中获得还是重数据库中获得，最终都会存储到本地缓存
            return itemCategoryTemp;
        });

        return itemCategory;
    }

    /**
     * 结合本地缓存Caffeine，优化数据库的查询效率，降低数据库的风险
     * @param id
     * @return
     */
    @GetMapping("get3")
    public ItemCategory getItemCategory3(Integer id) {

        String itemCategoryKey = "itemCategory:" + id;

        ItemCategory itemCategory = cache.get(itemCategoryKey, s -> {
            System.out.println("本地缓存中没有["+id+"]的值，现从数据库中查询后再返回。");
            return itemCategoryService.queryItemCategoryById(id);
        });

        return itemCategory;
    }

    @GetMapping("get2")
    public ItemCategory getItemCategory2(Integer id) {
        ItemCategory itemCategory = itemCategoryService.queryItemCategoryById(id);
        return itemCategory;
    }

    @PostMapping("create")
    public String createItemCategory() {
        Integer id = (int)((Math.random() * 9 + 1) * 100000);

        ItemCategory itemCategory = new ItemCategory();

        itemCategory.setId(id);
        itemCategory.setCategoryName("测试产品分类");

        itemCategoryService.createItemCategory(itemCategory);

        return "添加成功！";
    }

    //@PutMapping("update")
    //public String updateItemCategory(Integer id, String categoryName) throws Exception {
    //    // 1. 先删缓存
    //    String itemCategoryKey = "itemCategory:" + id;
    //    redisOperator.del(itemCategoryKey);
    //
    //    // 2. 再更新数据库
    //    itemCategoryService.updateItemCategory(id, categoryName);
    //
    //    // 3. sleep 300毫秒（视情况，可以延长或缩短）
    //    try {
    //        Thread.sleep(300);
    //    } catch (InterruptedException e) {
    //        e.printStackTrace();
    //    }
    //
    //    // 4. 再次删除缓存
    //    redisOperator.del(itemCategoryKey);
    //
    //    return "修改成功！";
    //}

    @PutMapping("update")
    public String updateItemCategory(Integer id, String categoryName) throws Exception {

        // 1. 先更新数据库
        itemCategoryService.updateItemCategory(id, categoryName);

        // 2. 后发异步消息队列，让消费者做缓存数据的一致性处理操作
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_SYNC_CACHE,
                                        RabbitMQConfig.ROUTING_KEY_SYNC_CACHE_MODIFY,
                                        String.valueOf(id));

        return "修改成功！";
    }

    @DeleteMapping("delete")
    public String deleteItemCategory(Integer id) {
        itemCategoryService.deleteItemCategoryById(id);
        return "删除成功！";
    }


    @GetMapping("getAfterConsumer")
    public Object getAfterConsumer(Integer id) {
        String itemCategoryKey = "itemCategory:" + id;
        return cache.getIfPresent(itemCategoryKey);
    }

}
