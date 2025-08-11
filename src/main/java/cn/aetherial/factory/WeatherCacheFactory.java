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
        RedisTemplate<String, Object> redisTemplate = null;
        
        try {
            redisTemplate = applicationContext.getBean("openWeatherRedisTemplate", RedisTemplate.class);
            log.info("Using openWeatherRedisTemplate bean for Redis cache");
            return new RedisWeatherCache(redisTemplate, cacheProperties);
        } catch (Exception e) {
            log.debug("No bean named 'openWeatherRedisTemplate' found, trying other strategies");
        }
        
        try {
            redisTemplate = applicationContext.getBean("redisTemplate", RedisTemplate.class);
            log.info("Using redisTemplate bean for Redis cache");
            return new RedisWeatherCache(redisTemplate, cacheProperties);
        } catch (Exception e) {
            log.debug("No bean named 'redisTemplate' found, trying other strategies");
        }
        
        try {
            String[] beanNames = applicationContext.getBeanNamesForType(RedisTemplate.class);
            if (beanNames.length > 0) {
                redisTemplate = applicationContext.getBean(beanNames[0], RedisTemplate.class);
                log.info("Using RedisTemplate bean '{}' for Redis cache", beanNames[0]);
                return new RedisWeatherCache(redisTemplate, cacheProperties);
            }
        } catch (Exception e) {
            log.debug("Error getting RedisTemplate beans", e);
        }
        
        // 如果所有策略都失败，抛出异常
        log.error("No RedisTemplate bean found in application context");
        throw new IllegalStateException("No RedisTemplate bean available for Redis cache");
    }
    
    private WeatherCache createLocalCache() {
        return new LocalWeatherCache(cacheProperties);
    }
}
