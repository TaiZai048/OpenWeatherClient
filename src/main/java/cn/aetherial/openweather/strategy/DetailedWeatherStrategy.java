package cn.aetherial.openweather.strategy;

import cn.aetherial.openweather.config.WeatherDetailsConfig;
import cn.aetherial.openweather.entity.*;
import cn.aetherial.openweather.utils.WeatherParserUtils;
import java.util.List;
import java.util.Map;

public class DetailedWeatherStrategy implements WeatherDataStrategy<WeatherDetails> {
    
    private WeatherDetailsConfig config;

    public DetailedWeatherStrategy() {
        this.config = WeatherDetailsConfig.createDefault();
    }

    public DetailedWeatherStrategy(WeatherDetailsConfig config) {
        this.config = config;
    }
    
    @Override
    public WeatherDetails extractWeatherData(Map<String, Object> data) {
        return extractWeatherData(data, this.config);
    }

    public WeatherDetails extractWeatherData(Map<String, Object> data, WeatherDetailsConfig config) {
        WeatherDetails weatherDetails = new WeatherDetails();

        weatherDetails.setLat(WeatherParserUtils.getDoubleValue(data, "lat"));
        weatherDetails.setLon(WeatherParserUtils.getDoubleValue(data, "lon"));
        weatherDetails.setTimezone(WeatherParserUtils.getStringValue(data, "timezone"));
        weatherDetails.setTimezoneOffset(WeatherParserUtils.getIntValue(data, "timezone_offset"));

        if (data.containsKey("current")) {
            Object currentObj = data.get("current");
            if (currentObj instanceof Map) {
                weatherDetails.setCurrent(WeatherParserUtils.parseCurrentWeather((Map<String, Object>) currentObj));
            }
        }

        if (config.isIncludeMinutely() && data.containsKey("minutely")) {
            List<MinutelyWeather> minutelyList = WeatherParserUtils.parseMinutelyList(data.get("minutely"));
            if (!minutelyList.isEmpty()) {
                weatherDetails.setMinutely(minutelyList);
            }
        }

        if (config.isIncludeHourly() && data.containsKey("hourly")) {
            List<HourlyWeather> hourlyList = WeatherParserUtils.parseHourlyList(data.get("hourly"));
            if (!hourlyList.isEmpty()) {
                weatherDetails.setHourly(hourlyList);
            }
        }

        if (config.isIncludeDaily() && data.containsKey("daily")) {
            List<DailyWeather> dailyList = WeatherParserUtils.parseDailyList(data.get("daily"));
            if (!dailyList.isEmpty()) {
                weatherDetails.setDaily(dailyList);
            }
        }

        if (config.isIncludeAlerts() && data.containsKey("alerts")) {
            List<Alerts> alertsList = WeatherParserUtils.parseAlertsList(data.get("alerts"));
            if (!alertsList.isEmpty()) {
                weatherDetails.setAlert(alertsList.get(0));
            }
        }
        
        return weatherDetails;
    }

    public void setConfig(WeatherDetailsConfig config) {
        this.config = config;
    }

    public WeatherDetailsConfig getConfig() {
        return this.config;
    }
}
