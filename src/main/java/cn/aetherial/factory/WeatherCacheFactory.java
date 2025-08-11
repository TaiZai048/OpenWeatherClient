package cn.aetherial.factory;

import cn.aetherial.cache.LocalWeatherCache;
import cn.aetherial.cache.NoOpWeatherCache;
import cn.aetherial.cache.RedisWeatherCache;
import cn.aetherial.cache.WeatherCache;
import cn.aetherial.properties.OpenWeatherCacheProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class WeatherCacheFactory {
    
    private static final Logger log = LoggerFactory.getLogger(WeatherCacheFactory.class);
    
    private final ApplicationContext applicationContext;
    private final OpenWeatherCacheProperties cacheProperties;
    
    @Autowired
    public WeatherCacheFactory(ApplicationContext applicationContext, OpenWeatherCacheProperties cacheProperties) {
        this.applicationContext = applicationContext;
        this.cacheProperties = cacheProperties;
    }

    public WeatherCache createCache() {
        if (!cacheProperties.isEnabled()) {
            log.info("Caching is disabled");
            return new NoOpWeatherCache();
        }
        
        String cacheType = cacheProperties.getType().toLowerCase();

        switch (cacheType) {
            case "redis":
                return createRedisCache();
            case "auto":
                try {
                    return createRedisCache();
                } catch (Exception e) {
                    return createLocalCache();
                }
            case "local":
            default:
                return createLocalCache();
        }
    }
    
    private WeatherCache createRedisCache() {
        RedisTemplate<String, Object> redisTemplate = applicationContext.getBean("openWeatherRedisTemplate", RedisTemplate.class);
        return new RedisWeatherCache(redisTemplate, cacheProperties);
    }
    
    private WeatherCache createLocalCache() {
        return new LocalWeatherCache(cacheProperties);
    }
}
