package cn.aetherial.config;

import cn.aetherial.cache.WeatherCache;
import cn.aetherial.factory.WeatherCacheFactory;
import cn.aetherial.properties.OpenWeatherCacheProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@EnableConfigurationProperties(OpenWeatherCacheProperties.class)
public class OpenWeatherCacheAutoConfiguration {
    
    private static final Logger log = LoggerFactory.getLogger(OpenWeatherCacheAutoConfiguration.class);

    @Bean
    @ConditionalOnMissingBean
    public RedisTemplate<String, Object> openWeatherRedisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);

        template.setKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());

        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        template.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());
        
        template.afterPropertiesSet();
        return template;
    }

    @Bean
    @ConditionalOnMissingBean
    public WeatherCacheFactory weatherCacheFactory(ApplicationContext applicationContext, OpenWeatherCacheProperties cacheProperties) {
        return new WeatherCacheFactory(applicationContext, cacheProperties);
    }
    
    @Bean
    @ConditionalOnMissingBean
    public WeatherCache weatherCache(WeatherCacheFactory cacheFactory) {
        return cacheFactory.createCache();
    }
}
