package cn.aetherial.openweather.strategy;

import java.util.Map;

public interface WeatherDataStrategy<T> {

    T extractWeatherData(Map<String, Object> data);
}
