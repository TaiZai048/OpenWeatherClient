package cn.aetherial.cache;

import cn.aetherial.properties.OpenWeatherCacheProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.concurrent.TimeUnit;

public class RedisWeatherCache implements WeatherCache {
    
    private static final Logger log = LoggerFactory.getLogger(RedisWeatherCache.class);
    
    private final RedisTemplate<String, Object> redisTemplate;
    private final OpenWeatherCacheProperties properties;
    
    public RedisWeatherCache(RedisTemplate<String, Object> redisTemplate, OpenWeatherCacheProperties properties) {
        this.redisTemplate = redisTemplate;
        this.properties = properties;
    }
    
    private String buildKey(String key) {
        return properties.getKeyPrefix() + key;
    }
    
    @Override
    public Object get(String key) {
        return redisTemplate.opsForValue().get(buildKey(key));
    }
    
    @Override
    public void put(String key, Object value, int expireSeconds) {
        String fullKey = buildKey(key);
        redisTemplate.opsForValue().set(fullKey, value);

        int actualExpireTime = expireSeconds > 0 ? expireSeconds : properties.getExpireTime();
        redisTemplate.expire(fullKey, actualExpireTime, TimeUnit.SECONDS);
    }
    
    @Override
    public void remove(String key) {
        redisTemplate.delete(buildKey(key));
    }
    
    @Override
    public void clear() {
        String pattern = properties.getKeyPrefix() + "*";
        redisTemplate.keys(pattern).forEach(redisTemplate::delete);
    }
    
    @Override
    public String getType() {
        return "redis";
    }
}
