package cn.aetherial.openweather.utils;

import cn.aetherial.openweather.entity.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class WeatherParserUtils {

    public static CurrentWeather parseCurrentWeather(Map<String, Object> data) {
        if (data == null) {
            return null;
        }
        
        CurrentWeather current = new CurrentWeather();

        setBaseWeatherProperties(current, data);

        current.setSunrise(getLongValue(data, "sunrise"));
        current.setSunset(getLongValue(data, "sunset"));
        current.setTemp(getDoubleValue(data, "temp"));
        current.setFeelsLike(getDoubleValue(data, "feels_like"));

        if (data.containsKey("weather")) {
            Object weatherObj = data.get("weather");
            if (weatherObj instanceof List<?>) {
                List<?> weatherList = (List<?>) weatherObj;
                if (!weatherList.isEmpty() && weatherList.get(0) instanceof Map) {
                    current.setWeather(parseWeather((Map<String, Object>) weatherList.get(0)));
                }
            }
        }
        
        return current;
    }

    public static List<MinutelyWeather> parseMinutelyList(Object data) {
        List<MinutelyWeather> result = new ArrayList<>();
        
        if (data instanceof List<?>) {
            List<?> rawList = (List<?>) data;
            for (Object item : rawList) {
                if (item instanceof Map) {
                    MinutelyWeather minutely = parseMinutelyWeather((Map<String, Object>) item);
                    if (minutely != null) {
                        result.add(minutely);
                    }
                }
            }
        }
        
        return result;
    }

    public static MinutelyWeather parseMinutelyWeather(Map<String, Object> data) {
        if (data == null) {
            return null;
        }
        
        MinutelyWeather minutely = new MinutelyWeather();
        
        minutely.setDt(getLongValue(data, "dt"));
        minutely.setPrecipitation(getDoubleValue(data, "precipitation"));
        
        return minutely;
    }

    public static List<HourlyWeather> parseHourlyList(Object data) {
        List<HourlyWeather> result = new ArrayList<>();
        
        if (data instanceof List<?>) {
            List<?> rawList = (List<?>) data;
            for (Object item : rawList) {
                if (item instanceof Map) {
                    HourlyWeather hourly = parseHourlyWeather((Map<String, Object>) item);
                    if (hourly != null) {
                        result.add(hourly);
                    }
                }
            }
        }
        
        return result;
    }

    public static HourlyWeather parseHourlyWeather(Map<String, Object> data) {
        if (data == null) {
            return null;
        }
        
        HourlyWeather hourly = new HourlyWeather();

        setBaseWeatherProperties(hourly, data);

        hourly.setPop(getDoubleValue(data, "pop"));
        hourly.setTemp(getDoubleValue(data, "temp"));
        hourly.setFeelsLike(getDoubleValue(data, "feels_like"));

        if (data.containsKey("weather")) {
            Object weatherObj = data.get("weather");
            if (weatherObj instanceof List<?>) {
                List<?> weatherList = (List<?>) weatherObj;
                if (!weatherList.isEmpty() && weatherList.get(0) instanceof Map) {
                    hourly.setWeather(parseWeather((Map<String, Object>) weatherList.get(0)));
                }
            }
        }
        
        return hourly;
    }

    public static List<DailyWeather> parseDailyList(Object data) {
        List<DailyWeather> result = new ArrayList<>();
        
        if (data instanceof List<?>) {
            List<?> rawList = (List<?>) data;
            for (Object item : rawList) {
                if (item instanceof Map) {
                    DailyWeather daily = parseDailyWeather((Map<String, Object>) item);
                    if (daily != null) {
                        result.add(daily);
                    }
                }
            }
        }
        
        return result;
    }

    public static DailyWeather parseDailyWeather(Map<String, Object> data) {
        if (data == null) {
            return null;
        }
        
        DailyWeather daily = new DailyWeather();

        setBaseWeatherProperties(daily, data);

        daily.setSunrise(getLongValue(data, "sunrise"));
        daily.setSunset(getLongValue(data, "sunset"));
        daily.setMoonrise(getLongValue(data, "moonrise"));
        daily.setMoonset(getLongValue(data, "moonset"));
        daily.setMoonPhase(getDoubleValue(data, "moon_phase"));
        daily.setSummary(getStringValue(data, "summary"));
        daily.setPop(getDoubleValue(data, "pop"));
        daily.setRain(getDoubleValue(data, "rain"));
        daily.setSnow(getDoubleValue(data, "snow"));

        if (data.containsKey("temp") && data.get("temp") instanceof Map) {
            daily.setTemp(parseTemp((Map<String, Object>) data.get("temp")));
        }

        if (data.containsKey("feels_like") && data.get("feels_like") instanceof Map) {
            daily.setFeelsLike(parseFeelsLike((Map<String, Object>) data.get("feels_like")));
        }

        if (data.containsKey("weather")) {
            Object weatherObj = data.get("weather");
            if (weatherObj instanceof List<?>) {
                List<?> weatherList = (List<?>) weatherObj;
                if (!weatherList.isEmpty() && weatherList.get(0) instanceof Map) {
                    daily.setWeather(parseWeather((Map<String, Object>) weatherList.get(0)));
                }
            }
        }
        
        return daily;
    }

    public static List<Alerts> parseAlertsList(Object data) {
        List<Alerts> result = new ArrayList<>();
        
        if (data instanceof List<?>) {
            List<?> rawList = (List<?>) data;
            for (Object item : rawList) {
                if (item instanceof Map) {
                    Alerts alert = parseAlerts((Map<String, Object>) item);
                    if (alert != null) {
                        result.add(alert);
                    }
                }
            }
        }
        
        return result;
    }

    public static Alerts parseAlerts(Map<String, Object> data) {
        if (data == null) {
            return null;
        }
        
        Alerts alert = new Alerts();
        
        alert.setSenderName(getStringValue(data, "sender_name"));
        alert.setEvent(getStringValue(data, "event"));
        alert.setStart(getLongValue(data, "start"));
        alert.setEnd(getLongValue(data, "end"));
        alert.setDescription(getStringValue(data, "description"));

        if (data.containsKey("tags")) {
            Object tagsObj = data.get("tags");
            if (tagsObj instanceof List) {
                List<String> tagsList = new ArrayList<>();
                for (Object tag : (List<?>) tagsObj) {
                    if (tag != null) {
                        tagsList.add(tag.toString());
                    }
                }
                if (!tagsList.isEmpty()) {
                    alert.setTags(String.join(",", tagsList));
                }
            } else if (tagsObj instanceof String) {
                alert.setTags((String) tagsObj);
            }
        }
        
        return alert;
    }

    public static Temp parseTemp(Map<String, Object> data) {
        if (data == null) {
            return null;
        }
        
        Temp temp = new Temp();
        
        temp.setMorn(getDoubleValue(data, "morn"));
        temp.setDay(getDoubleValue(data, "day"));
        temp.setEve(getDoubleValue(data, "eve"));
        temp.setNight(getDoubleValue(data, "night"));
        temp.setMin(getDoubleValue(data, "min"));
        temp.setMax(getDoubleValue(data, "max"));
        
        return temp;
    }

    public static FeelsLike parseFeelsLike(Map<String, Object> data) {
        if (data == null) {
            return null;
        }
        
        FeelsLike feelsLike = new FeelsLike();
        
        feelsLike.setMorn(getDoubleValue(data, "morn"));
        feelsLike.setDay(getDoubleValue(data, "day"));
        feelsLike.setEve(getDoubleValue(data, "eve"));
        feelsLike.setNight(getDoubleValue(data, "night"));
        
        return feelsLike;
    }

    public static Weather parseWeather(Map<String, Object> data) {
        if (data == null) {
            return null;
        }
        
        Weather weather = new Weather();
        
        weather.setId(getIntValue(data, "id"));
        weather.setMain(getStringValue(data, "main"));
        weather.setDescription(getStringValue(data, "description"));
        weather.setIcon(getStringValue(data, "icon"));
        
        return weather;
    }

    public static void setBaseWeatherProperties(BaseWeather weather, Map<String, Object> data) {
        weather.setDt(getLongValue(data, "dt"));
        weather.setPressure(getIntValue(data, "pressure"));
        weather.setHumidity(getIntValue(data, "humidity"));
        weather.setDewPoint(getDoubleValue(data, "dew_point"));
        weather.setClouds(getIntValue(data, "clouds"));
        weather.setUvi(getDoubleValue(data, "uvi"));
        weather.setVisibility(getIntValue(data, "visibility"));
        weather.setWindSpeed(getDoubleValue(data, "wind_speed"));
        weather.setWindGust(getDoubleValue(data, "wind_gust"));
        weather.setWindDeg(getIntValue(data, "wind_deg"));
    }

    public static Double getDoubleValue(Map<String, Object> data, String key) {
        if (data != null && data.containsKey(key) && data.get(key) != null) {
            Object value = data.get(key);
            if (value instanceof Number) {
                return ((Number) value).doubleValue();
            }
        }
        return null;
    }

    public static Integer getIntValue(Map<String, Object> data, String key) {
        if (data != null && data.containsKey(key) && data.get(key) != null) {
            Object value = data.get(key);
            if (value instanceof Number) {
                return ((Number) value).intValue();
            }
        }
        return null;
    }

    public static Long getLongValue(Map<String, Object> data, String key) {
        if (data != null && data.containsKey(key) && data.get(key) != null) {
            Object value = data.get(key);
            if (value instanceof Number) {
                return ((Number) value).longValue();
            }
        }
        return null;
    }

    public static String getStringValue(Map<String, Object> data, String key) {
        if (data != null && data.containsKey(key) && data.get(key) != null) {
            return data.get(key).toString();
        }
        return null;
    }
    

}
