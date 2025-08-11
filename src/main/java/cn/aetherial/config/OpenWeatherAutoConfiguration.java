package cn.aetherial.config;

import cn.aetherial.cache.WeatherCache;
import cn.aetherial.factory.WeatherDataStrategyFactory;
import cn.aetherial.properties.OpenWeatherProperties;
import cn.aetherial.service.OpenWeatherService;
import cn.aetherial.strategy.DetailedWeatherStrategy;
import cn.aetherial.strategy.SimpleWeatherStrategy;
import cn.aetherial.strategy.StandardWeatherStrategy;
import cn.aetherial.utils.OpenWeatherUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(OpenWeatherProperties.class)
public class OpenWeatherAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public SimpleWeatherStrategy simpleWeatherStrategy() {
        return new SimpleWeatherStrategy();
    }
    
    @Bean
    @ConditionalOnMissingBean
    public StandardWeatherStrategy standardWeatherStrategy() {
        return new StandardWeatherStrategy();
    }
    
    @Bean
    @ConditionalOnMissingBean
    public DetailedWeatherStrategy detailedWeatherStrategy(WeatherDetailsConfig config) {
        return new DetailedWeatherStrategy(config);
    }
    
    @Bean
    @ConditionalOnMissingBean
    public WeatherDetailsConfig weatherDetailsConfig() {
        return WeatherDetailsConfig.createDefault();
    }
    
    @Bean
    @ConditionalOnMissingBean
    public WeatherDataStrategyFactory weatherDataStrategyFactory(
            SimpleWeatherStrategy simpleStrategy,
            StandardWeatherStrategy standardStrategy,
            DetailedWeatherStrategy detailedStrategy) {
        return new WeatherDataStrategyFactory(simpleStrategy, standardStrategy, detailedStrategy);
    }

    @Bean
    @ConditionalOnMissingBean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean
    @ConditionalOnMissingBean
    public OpenWeatherService openWeatherService(
            RestTemplateBuilder restTemplateBuilder,
            OpenWeatherProperties properties,
            WeatherDataStrategyFactory strategyFactory,
            WeatherCache weatherCache,
            ObjectMapper objectMapper) {
        return new OpenWeatherService(restTemplateBuilder, properties, strategyFactory, weatherCache, objectMapper);
    }

    @Bean
    @ConditionalOnMissingBean
    public OpenWeatherUtils openWeatherUtils() {
        return new OpenWeatherUtils();
    }
}
