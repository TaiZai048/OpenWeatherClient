package cn.aetherial.openweather.cache;

import cn.aetherial.openweather.properties.OpenWeatherCacheProperties;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public class LocalWeatherCache implements WeatherCache {
    
    private static final Logger log = LoggerFactory.getLogger(LocalWeatherCache.class);
    
    private final Cache<String, Object> cache;
    private final OpenWeatherCacheProperties properties;
    
    public LocalWeatherCache(OpenWeatherCacheProperties properties) {
        this.properties = properties;
        this.cache = Caffeine.newBuilder()
                .maximumSize(properties.getMaxSize())
                .expireAfterWrite(properties.getExpireTime(), TimeUnit.SECONDS)
                .build();
    }
    
    @Override
    public Object get(String key) {
        return cache.getIfPresent(key);
    }
    
    @Override
    public void put(String key, Object value, int expireSeconds) {
        cache.put(key, value);
    }
    
    @Override
    public void remove(String key) {
        cache.invalidate(key);
    }
    
    @Override
    public void clear() {
        cache.invalidateAll();
    }
    
    @Override
    public String getType() {
        return "local";
    }
}
