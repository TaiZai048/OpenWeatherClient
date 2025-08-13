package cn.aetherial.openweather.strategy;

import cn.aetherial.openweather.entity.Weather;
import cn.aetherial.openweather.entity.WeatherSimple;
import cn.aetherial.openweather.utils.WeatherParserUtils;
import java.util.List;
import java.util.Map;

public class SimpleWeatherStrategy implements WeatherDataStrategy<WeatherSimple> {
    
    @Override
    public WeatherSimple extractWeatherData(Map<String, Object> data) {
        WeatherSimple weatherSimple = new WeatherSimple();

        weatherSimple.setLat(WeatherParserUtils.getDoubleValue(data, "lat"));
        weatherSimple.setLon(WeatherParserUtils.getDoubleValue(data, "lon"));
        weatherSimple.setTimezone(WeatherParserUtils.getStringValue(data, "timezone"));
        weatherSimple.setTimezoneOffset(WeatherParserUtils.getIntValue(data, "timezone_offset"));

        if (data.containsKey("current")) {
            Object currentObj = data.get("current");
            if (currentObj instanceof Map) {
                Map<String, Object> currentData = (Map<String, Object>) currentObj;
                WeatherSimple.CurrentSimple current = new WeatherSimple.CurrentSimple();

                current.setTemp(WeatherParserUtils.getDoubleValue(currentData, "temp"));
                current.setFeelsLike(WeatherParserUtils.getDoubleValue(currentData, "feels_like"));
                current.setHumidity(WeatherParserUtils.getIntValue(currentData, "humidity"));

                if (currentData.containsKey("weather")) {
                    Object weatherObj = currentData.get("weather");
                    if (weatherObj instanceof List && !((List<?>)weatherObj).isEmpty()) {
                        Object weatherItemObj = ((List<?>)weatherObj).get(0);
                        if (weatherItemObj instanceof Map) {
                            Map<String, Object> weatherItemMap = (Map<String, Object>)weatherItemObj;
                            Weather weatherInfo = new Weather();
                            weatherInfo.setId(WeatherParserUtils.getIntValue(weatherItemMap, "id"));
                            weatherInfo.setMain(WeatherParserUtils.getStringValue(weatherItemMap, "main"));
                            weatherInfo.setDescription(WeatherParserUtils.getStringValue(weatherItemMap, "description"));
                            weatherInfo.setIcon(WeatherParserUtils.getStringValue(weatherItemMap, "icon"));
                            current.setWeather(weatherInfo);
                        }
                    }
                }
                
                weatherSimple.setCurrent(current);
            }
        }
        
        return weatherSimple;
    }
}
