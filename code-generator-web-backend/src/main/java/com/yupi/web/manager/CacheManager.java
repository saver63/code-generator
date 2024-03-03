package com.yupi.web.manager;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * 多级缓存
 *
 * @author <a href="https://github.com/liyupi">程序员鱼皮</a>
 * @from <a href="https://yupi.icu">编程导航知识星球</a>
 */
@Component
public class CacheManager {

    @Resource
    private RedisTemplate<String,Object> redisTemplate;

    /**
     * 构建本地缓存
     */
    Cache<String, Object> localCache = Caffeine.newBuilder()
            .expireAfterWrite(100, TimeUnit.MINUTES)
            .maximumSize(10_000)
            .build();

    /**
     * 更新缓存
     *
     * @param key
     * @param value
     */
    public void put(String key, Object value) {
        // 添加或者更新一个缓存元素
        localCache.put(key, value);
        //写入缓存
        //注意：一定要设置过期时间
        redisTemplate.opsForValue().set(key, value, 100, TimeUnit.MINUTES);
    }

    /**
     * 写入缓存
     *
     * @param key
     * @return
     */
    public Object get(String key) {


        // 先从本地缓存查
        Object value = localCache.getIfPresent(key);
        if (value != null) {
            return value;
        }

        //本地缓存未命中，尝试从分布式缓存中查询
        value = redisTemplate.opsForValue().get(key);
        if (value != null) {
            //将redis缓存写入到本地缓存
            localCache.put(key, value);
        }

        return value;
    }

    /**
     * 删除缓存
     *
     * @param key
     */
    public void deletes(String key) {
        // 移除一个缓存元素
        localCache.invalidate(key);
        redisTemplate.delete(key);
    }


}
