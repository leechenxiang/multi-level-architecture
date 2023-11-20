package com.itzixi;

import com.github.benmanes.caffeine.cache.Cache;
import com.itzixi.pojo.ItemCategory;
import com.itzixi.service.ItemCategoryService;
import com.itzixi.utils.JsonUtils;
import com.itzixi.utils.RedisOperator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SystemCachePrepareConfig implements CommandLineRunner {

    @Autowired
    private ItemCategoryService itemCategoryService;

    @Autowired
    private Cache<String, List<ItemCategory>> categoryListCache;

    @Autowired
    private RedisOperator redisOperator;

    /**
     * 系统启动进入本方法，用于缓存预热，提前准备数据
     * @param args
     * @throws Exception
     */
    @Override
    public void run(String... args) {
        // 1. 查询所有商品分类数据
        List<ItemCategory> typeList = itemCategoryService.queryItemCategoryList();

        String itemCategoryListKey = "itemCategoryList";

        // 2. 设置分类数据到本地缓存
        categoryListCache.put(itemCategoryListKey, typeList);

        // 3. 设置分类数据到redis
        redisOperator.set(itemCategoryListKey, JsonUtils.objectToJson(typeList));
    }
}
