package com.itzixi.service.impl;

import com.itzixi.mapper.ItemCategoryMapper;
import com.itzixi.pojo.ItemCategory;
import com.itzixi.service.ItemCategoryService;
import com.itzixi.utils.RedisOperator;
import org.redisson.RedissonMultiLock;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;
import java.util.concurrent.locks.ReentrantLock;

@Service
public class ItemCategoryServiceImpl implements ItemCategoryService {

    @Autowired
    private ItemCategoryMapper itemCategoryMapper;

    private ReentrantLock reentrantLock;

    //@Transactional
    //@Override
    //public void updateItemCategory(Integer categoryId, String categoryName) {
    //    // 加锁
    //    reentrantLock.lock();
    //
    //    // 执行业务
    //    ItemCategory pending = new ItemCategory();
    //    pending.setId(categoryId);
    //    pending.setCategoryName(categoryName);
    //
    //    itemCategoryMapper.updateByPrimaryKey(pending);
    //
    //    // 解锁
    //    reentrantLock.unlock();
    //}

    @Autowired
    private RedissonClient redissonClient;

    @Transactional
    @Override
    public void updateItemCategory(Integer categoryId, String categoryName) {

        // 定义锁的名称
        String redisLock = "redisson-lock";
        // 声明锁
        //RLock rLock = redissonClient.getLock(redisLock);
        RLock rLock = redissonClient.getFairLock(redisLock);
        // 加锁
        rLock.lock();

        try {
            ItemCategory pending = new ItemCategory();
            pending.setId(categoryId);
            pending.setCategoryName(categoryName);

            itemCategoryMapper.updateByPrimaryKey(pending);
        } finally {
            // 释放锁
            rLock.unlock();
        }
    }

    //public void updateItemCategoryMultiLock(Integer categoryId, String categoryName) {
    //    // 定义锁的名称
    //    String redisLock1 = "redisson-multi-lock-1";
    //    String redisLock2 = "redisson-multi-lock-1";
    //    String redisLock3 = "redisson-multi-lock-1";
    //    // 声明锁
    //    RLock multiLock1 = redissonClient.getLock(redisLock1);
    //    RLock multiLock2 = redissonClient.getLock(redisLock2);
    //    RLock multiLock3 = redissonClient.getLock(redisLock3);
    //    RedissonMultiLock locks = new RedissonMultiLock(multiLock1, multiLock2, multiLock3);
    //    // 加锁
    //    locks.lock();
    //
    //    try {
    //        ItemCategory pending = new ItemCategory();
    //        pending.setId(categoryId);
    //        pending.setCategoryName(categoryName);
    //
    //        itemCategoryMapper.updateByPrimaryKey(pending);
    //    } finally {
    //        // 释放锁
    //        locks.unlock();
    //    }
    //}

    @Autowired
    private RedisOperator redisOperator;

    //@Transactional
    //@Override
    //public void updateItemCategory(Integer categoryId, String categoryName) throws Exception {
    //
    //
    //    String distLock = "redis-lock";
    //    String selfId = UUID.randomUUID().toString();
    //    Integer expireTimes = 30;
    //
    //    while (!redisOperator.setnx(distLock, selfId, expireTimes)) {
    //        // 如果加锁失败，则重试循环
    //        System.out.println("setnx 锁生效中，一会重试~");
    //        Thread.sleep(50);
    //    }
    //
    //    // 一旦获得锁，则开启新的timer执行定期检查，做lock的自动续期
    //    autoRefreshLockTimes(distLock, selfId, expireTimes);
    //
    //
    //    try {
    //        // 加锁成功，执行业务
    //        ItemCategory pending = new ItemCategory();
    //        pending.setId(categoryId);
    //        pending.setCategoryName(categoryName);
    //
    //        Thread.sleep(40000);
    //
    //        itemCategoryMapper.updateByPrimaryKey(pending);
    //
    //    } finally {
    //        // 业务执行完毕，释放锁
    //        // 使用LUA脚本执行删除key操作，为了保证原子性
    //        String lockScript =
    //                " if redis.call('get',KEYS[1]) == ARGV[1] "
    //                        + " then "
    //                        +   " return redis.call('del',KEYS[1]) "
    //                        + " else "
    //                        +   " return 0 "
    //                        + " end ";
    //        long unLockResult = redisOperator.execLuaScript(lockScript, distLock, selfId);
    //
    //        if (unLockResult == 1) {
    //            lockTimer.cancel();
    //            System.out.println("释放锁，并且取消timer~");
    //        }
    //    }
    //}
    //
    //private Timer lockTimer = new Timer();
    //// 自动续期
    //private void autoRefreshLockTimes(String distLock, String selfId, Integer expireTimes) {
    //
    //    String refreshScript =
    //            " if redis.call('get',KEYS[1]) == ARGV[1] "
    //                    + " then "
    //                    +   " return redis.call('expire',KEYS[1],30) "
    //                    + " else "
    //                    +   " return 0 "
    //                    + " end ";
    //    lockTimer.schedule(new TimerTask() {
    //                           @Override
    //                           public void run() {
    //                               System.out.println("自动续期，重置30秒");
    //                               redisOperator.execLuaScript(refreshScript, distLock, selfId);
    //                           }
    //                       },
    //            expireTimes/3*1000,
    //            expireTimes/3*1000);
    //}


    @Override
    public ItemCategory queryItemCategoryById(Integer categoryId) {
        return itemCategoryMapper.selectByPrimaryKey(categoryId);
    }

    @Override
    public void createItemCategory(ItemCategory itemCategory) {
        itemCategoryMapper.insert(itemCategory);
    }

    //@Transactional
    //@Override
    //public synchronized void updateItemCategory(Integer categoryId, String categoryName) {
    //
    //    ItemCategory pending = new ItemCategory();
    //    pending.setId(categoryId);
    //    pending.setCategoryName(categoryName);
    //
    //    itemCategoryMapper.updateByPrimaryKey(pending);
    //}

    @Override
    public void deleteItemCategoryById(Integer categoryId) {
        itemCategoryMapper.deleteByPrimaryKey(categoryId);
    }

    @Override
    public List<ItemCategory> queryItemCategoryList() {
        return itemCategoryMapper.selectAll();
    }
}
