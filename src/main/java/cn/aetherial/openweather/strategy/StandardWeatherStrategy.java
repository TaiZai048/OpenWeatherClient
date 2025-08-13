package cn.aetherial.openweather.strategy;

import cn.aetherial.openweather.entity.WeatherStandard;
import cn.aetherial.openweather.utils.WeatherParserUtils;
import java.util.Map;

public class StandardWeatherStrategy implements WeatherDataStrategy<WeatherStandard> {
    
    @Override
    public WeatherStandard extractWeatherData(Map<String, Object> data) {
        WeatherStandard weatherStandard = new WeatherStandard();

        weatherStandard.setLat(WeatherParserUtils.getDoubleValue(data, "lat"));
        weatherStandard.setLon(WeatherParserUtils.getDoubleValue(data, "lon"));
        weatherStandard.setTimezone(WeatherParserUtils.getStringValue(data, "timezone"));
        weatherStandard.setTimezoneOffset(WeatherParserUtils.getIntValue(data, "timezone_offset"));

        if (data.containsKey("current")) {
            Object currentObj = data.get("current");
            if (currentObj instanceof Map) {
                weatherStandard.setCurrent(WeatherParserUtils.parseCurrentWeather((Map<String, Object>) currentObj));
            }
        }
        
        return weatherStandard;
    }
}
