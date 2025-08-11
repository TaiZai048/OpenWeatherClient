package cn.aetherial.factory;

import cn.aetherial.entity.WeatherDetails;
import cn.aetherial.entity.WeatherSimple;
import cn.aetherial.entity.WeatherStandard;
import cn.aetherial.enums.WeatherDetailLevel;
import cn.aetherial.strategy.DetailedWeatherStrategy;
import cn.aetherial.strategy.SimpleWeatherStrategy;
import cn.aetherial.strategy.StandardWeatherStrategy;
import cn.aetherial.strategy.WeatherDataStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.EnumMap;
import java.util.Map;

public class WeatherDataStrategyFactory {
    
    private final Map<WeatherDetailLevel, WeatherDataStrategy<?>> strategies;

    @Autowired
    public WeatherDataStrategyFactory(
            @Qualifier("simpleWeatherStrategy") SimpleWeatherStrategy simpleStrategy,
            @Qualifier("standardWeatherStrategy") StandardWeatherStrategy standardStrategy,
            @Qualifier("detailedWeatherStrategy") DetailedWeatherStrategy detailedStrategy) {

        this.strategies = new EnumMap<>(WeatherDetailLevel.class);
        strategies.put(WeatherDetailLevel.SIMPLE, simpleStrategy);
        strategies.put(WeatherDetailLevel.STANDARD, standardStrategy);
        strategies.put(WeatherDetailLevel.DETAILED, detailedStrategy);
    }

    @SuppressWarnings("unchecked")
    public WeatherDataStrategy<WeatherSimple> getSimpleStrategy() {
        return (WeatherDataStrategy<WeatherSimple>) strategies.get(WeatherDetailLevel.SIMPLE);
    }

    @SuppressWarnings("unchecked")
    public WeatherDataStrategy<WeatherStandard> getStandardStrategy() {
        return (WeatherDataStrategy<WeatherStandard>) strategies.get(WeatherDetailLevel.STANDARD);
    }

    @SuppressWarnings("unchecked")
    public WeatherDataStrategy<WeatherDetails> getDetailedStrategy() {
        return (WeatherDataStrategy<WeatherDetails>) strategies.get(WeatherDetailLevel.DETAILED);
    }

    @SuppressWarnings("unchecked")
    public <T> WeatherDataStrategy<T> getStrategy(WeatherDetailLevel level) {
        return (WeatherDataStrategy<T>) strategies.get(level);
    }
}
