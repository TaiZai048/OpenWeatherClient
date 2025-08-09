package cn.aetherial.strategy;

import java.util.Map;

public interface WeatherDataStrategy<T> {

    T extractWeatherData(Map<String, Object> data);
}
