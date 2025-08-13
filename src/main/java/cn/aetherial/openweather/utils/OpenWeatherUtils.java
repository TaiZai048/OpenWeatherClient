package cn.aetherial.openweather.utils;

import cn.aetherial.openweather.config.WeatherDetailsConfig;
import cn.aetherial.openweather.entity.CountryInfo;
import cn.aetherial.openweather.entity.WeatherSimple;
import cn.aetherial.openweather.entity.WeatherStandard;
import cn.aetherial.openweather.enums.WeatherDetailLevel;
import cn.aetherial.openweather.entity.WeatherDetails;
import cn.aetherial.openweather.service.OpenWeatherService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class OpenWeatherUtils implements ApplicationContextAware {

    private static OpenWeatherService openWeatherService;

    public static WeatherStandard getWeather(double lat, double lon) {
        checkServiceAvailability();
        return openWeatherService.getWeatherByCoordinates(lat, lon);
    }

    public static <T> T getWeather(double lat, double lon, WeatherDetailLevel level) {
        checkServiceAvailability();
        return openWeatherService.getWeatherByCoordinates(lat, lon, level);
    }

    public static WeatherSimple getSimpleWeather(double lat, double lon) {
        checkServiceAvailability();
        return openWeatherService.getSimpleWeatherByCoordinates(lat, lon);
    }

    public static WeatherDetails getDetailedWeather(double lat, double lon) {
        checkServiceAvailability();
        return openWeatherService.getDetailedWeatherByCoordinates(lat, lon);
    }

    public static WeatherDetails getDetailedWeather(double lat, double lon, WeatherDetailsConfig config) {
        checkServiceAvailability();
        return openWeatherService.getDetailedWeatherByCoordinates(lat, lon, config);
    }

    public static WeatherStandard getWeatherByCity(String cityName) {
        checkServiceAvailability();
        CountryInfo countryInfo = openWeatherService.getCityInfo(cityName, null);
        if (countryInfo == null) {
            return null;
        }
        return getWeather(countryInfo.getLat(), countryInfo.getLon());
    }

    public static WeatherStandard getWeatherByCity(String cityName, String countryCode) {
        checkServiceAvailability();
        CountryInfo countryInfo = openWeatherService.getCityInfo(cityName, countryCode);
        if (countryInfo == null) {
            return null;
        }
        return getWeather(countryInfo.getLat(), countryInfo.getLon());
    }

    public static <T> T getWeatherByCity(String cityName, WeatherDetailLevel level) {
        checkServiceAvailability();
        CountryInfo countryInfo = openWeatherService.getCityInfo(cityName, null);
        if (countryInfo == null) {
            return null;
        }
        return getWeather(countryInfo.getLat(), countryInfo.getLon(), level);
    }

    public static <T> T getWeatherByCity(String cityName, String countryCode, WeatherDetailLevel level) {
        checkServiceAvailability();
        CountryInfo countryInfo = openWeatherService.getCityInfo(cityName, countryCode);
        if (countryInfo == null) {
            return null;
        }
        return getWeather(countryInfo.getLat(), countryInfo.getLon(), level);
    }

    public static WeatherSimple getSimpleWeatherByCity(String cityName) {
        checkServiceAvailability();
        CountryInfo countryInfo = openWeatherService.getCityInfo(cityName, null);
        if (countryInfo == null) {
            return null;
        }
        return getSimpleWeather(countryInfo.getLat(), countryInfo.getLon());
    }

    public static WeatherSimple getSimpleWeatherByCity(String cityName, String countryCode) {
        checkServiceAvailability();
        CountryInfo countryInfo = openWeatherService.getCityInfo(cityName, countryCode);
        if (countryInfo == null) {
            return null;
        }
        return getSimpleWeather(countryInfo.getLat(), countryInfo.getLon());
    }

    public static WeatherDetails getDetailedWeatherByCity(String cityName) {
        checkServiceAvailability();
        CountryInfo countryInfo = openWeatherService.getCityInfo(cityName, null);
        if (countryInfo == null) {
            return null;
        }
        return getDetailedWeather(countryInfo.getLat(), countryInfo.getLon());
    }

    public static WeatherDetails getDetailedWeatherByCity(String cityName, WeatherDetailsConfig config) {
        checkServiceAvailability();
        CountryInfo countryInfo = openWeatherService.getCityInfo(cityName, null);
        if (countryInfo == null) {
            return null;
        }
        return getDetailedWeather(countryInfo.getLat(), countryInfo.getLon(), config);
    }

    public static WeatherDetails getDetailedWeatherByCity(String cityName, String countryCode) {
        checkServiceAvailability();
        CountryInfo countryInfo = openWeatherService.getCityInfo(cityName, countryCode);
        if (countryInfo == null) {
            return null;
        }
        return getDetailedWeather(countryInfo.getLat(), countryInfo.getLon());
    }

    public static WeatherDetails getDetailedWeatherByCity(String cityName, String countryCode, WeatherDetailsConfig config) {
        checkServiceAvailability();
        CountryInfo countryInfo = openWeatherService.getCityInfo(cityName, countryCode);
        if (countryInfo == null) {
            return null;
        }
        return getDetailedWeather(countryInfo.getLat(), countryInfo.getLon(), config);
    }
    
    /**
     * 检查服务是否可用
     */
    private static void checkServiceAvailability() {
        if (openWeatherService == null) {
            throw new IllegalStateException("OpenWeatherService is not initialized. Please make sure that the Spring application context is started.");
        }
    }
    
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        openWeatherService = applicationContext.getBean(OpenWeatherService.class);
    }
}
