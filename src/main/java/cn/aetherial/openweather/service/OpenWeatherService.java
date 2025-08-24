package cn.aetherial.openweather.service;

import cn.aetherial.openweather.cache.WeatherCache;
import cn.aetherial.openweather.config.WeatherDetailsConfig;
import cn.aetherial.openweather.constant.WeatherConstants;
import cn.aetherial.openweather.constant.WeatherConstants.Validation;
import cn.aetherial.openweather.entity.CountryInfo;
import cn.aetherial.openweather.entity.WeatherDetails;
import cn.aetherial.openweather.entity.WeatherSimple;
import cn.aetherial.openweather.entity.WeatherStandard;
import cn.aetherial.openweather.enums.WeatherDetailLevel;
import cn.aetherial.openweather.exception.OpenWeatherException;
import cn.aetherial.openweather.exception.OpenWeatherException.ErrorCodes;
import cn.aetherial.openweather.factory.WeatherDataStrategyFactory;
import cn.aetherial.openweather.properties.OpenWeatherProperties;
import cn.aetherial.openweather.strategy.DetailedWeatherStrategy;
import cn.aetherial.openweather.strategy.WeatherDataStrategy;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.client.RestClientException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.concurrent.ConcurrentHashMap;

import java.time.Duration;

import java.util.Map;

import static cn.aetherial.openweather.constant.WeatherConstants.Api.*;

public class OpenWeatherService {

    private static final Logger log = LoggerFactory.getLogger(OpenWeatherService.class);
    
    private final RestTemplate restTemplate;
    private final OpenWeatherProperties properties;
    private final WeatherDataStrategyFactory strategyFactory;
    private final WeatherCache weatherCache;
    private final ObjectMapper objectMapper;
    private final Map<String, Object> keyLocks = new ConcurrentHashMap<>();

    @Autowired
    public OpenWeatherService(RestTemplate restTemplate, OpenWeatherProperties properties, 
                             WeatherDataStrategyFactory strategyFactory, WeatherCache weatherCache, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.properties = properties;
        this.strategyFactory = strategyFactory;
        this.weatherCache = weatherCache;
        this.objectMapper = objectMapper;
        
        log.info("OpenWeatherService initialization is complete, using cache type: {}", weatherCache.getType());
    }

    private String buildWeatherUrl(double lat, double lon) {
        String fullUrl = properties.getApiDomain() + WeatherConstants.Api.WEATHER_PATH;
        return UriComponentsBuilder.fromHttpUrl(fullUrl)
                .queryParam(PARAM_LAT, lat)
                .queryParam(PARAM_LON, lon)
                .queryParam(PARAM_API_KEY, properties.getApiKey())
                .queryParam(PARAM_UNITS, properties.getUnits())
                .queryParam(PARAM_LANG, properties.getLang())
                .build()
                .toUriString();
    }

    private String buildGeoUrl(String cityName, String countryCode) {
        String query = cityName;
        if (countryCode != null && !countryCode.isEmpty()) {
            query += "," + countryCode;
        }
        String fullUrl = properties.getApiDomain() + WeatherConstants.Api.GEOCODING_PATH;
        return UriComponentsBuilder.fromHttpUrl(fullUrl)
                .queryParam(PARAM_CITY, query)
                .queryParam(PARAM_API_KEY, properties.getApiKey())
                .queryParam(PARAM_LIMIT, 1)
                .build()
                .toUriString();
    }

    public WeatherStandard getWeatherByCoordinates(double lat, double lon) {
        return getWeatherByCoordinates(lat, lon, WeatherDetailLevel.STANDARD);
    }

    public WeatherSimple getSimpleWeatherByCoordinates(double lat, double lon) {
        Map<String, Object> response = fetchWeatherData(lat, lon);
        return strategyFactory.getSimpleStrategy().extractWeatherData(response);
    }

    public WeatherDetails getDetailedWeatherByCoordinates(double lat, double lon) {
        return getDetailedWeatherByCoordinates(lat, lon, WeatherDetailsConfig.createDefault());
    }

    public WeatherDetails getDetailedWeatherByCoordinates(double lat, double lon, WeatherDetailsConfig config) {
        Map<String, Object> response = fetchWeatherData(lat, lon);
        DetailedWeatherStrategy detailedStrategy = (DetailedWeatherStrategy) strategyFactory.getDetailedStrategy();
        return detailedStrategy.extractWeatherData(response, config);
    }

    public <T> T getWeatherByCoordinates(double lat, double lon, WeatherDetailLevel level) {
        Map<String, Object> response = fetchWeatherData(lat, lon);
        WeatherDataStrategy<T> strategy = strategyFactory.getStrategy(level);
        return strategy.extractWeatherData(response);
    }

    private Map<String, Object> fetchWeatherData(double lat, double lon) {
        validateCoordinates(lat, lon);
        String cacheKey = buildCacheKey(lat, lon);

        Map<String, Object> cachedData = getCachedWeatherData(cacheKey);
        if (cachedData != null) {
            return cachedData;
        }

        Object lock = keyLocks.computeIfAbsent(cacheKey, k -> new Object());
        synchronized (lock) {
            try {
                cachedData = getCachedWeatherData(cacheKey);
                if (cachedData != null) {
                    return cachedData;
                }

                String url = buildWeatherUrl(lat, lon);
                
                try {
                    Map<String, Object> response = restTemplate.getForObject(url, Map.class);
                    if (response == null) {
                        String errorMsg = String.format("Failed to get weather data from API: lat=%s, lon=%s", lat, lon);
                        log.error(errorMsg);
                        throw new OpenWeatherException(errorMsg, ErrorCodes.API_REQUEST_FAILED);
                    }

                    cacheWeatherData(cacheKey, response);
                    
                    return response;
                } catch (RestClientException e) {
                    String errorMsg = String.format("An exception occurred when requesting weather data: %s", e.getMessage());
                    log.error(errorMsg);
                    throw new OpenWeatherException(errorMsg, ErrorCodes.API_REQUEST_FAILED, e);
                }
            } finally {
                keyLocks.remove(cacheKey);
            }
        }
    }

    private String buildCacheKey(double lat, double lon) {
        return String.format("weather:lat:%s:lon:%s:units:%s:lang:%s", 
                lat, lon, properties.getUnits(), properties.getLang());
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> getCachedWeatherData(String cacheKey) {
        try {
            Object cachedData = weatherCache.get(cacheKey);
            if (cachedData != null) {
                if (cachedData instanceof Map) {
                    return (Map<String, Object>) cachedData;
                } else if (cachedData instanceof String) {
                    return objectMapper.readValue((String) cachedData, Map.class);
                }
            }
        } catch (Exception e) {
            log.warn("Failed to retrieve weather data from cache: {}", e.getMessage());
        }
        return null;
    }

    private void cacheWeatherData(String cacheKey, Map<String, Object> weatherData) {
        try {
            weatherCache.put(cacheKey, weatherData, 0);
        } catch (Exception ignored) {
        }
    }

    private void validateCoordinates(double lat, double lon) {
        StringBuilder errorMsg = new StringBuilder();
        
        if (!isValidLatitude(lat)) {
            errorMsg.append(String.format("The latitude value %s is out of valid range [%s, %s]",
                    lat, Validation.MIN_LATITUDE, Validation.MAX_LATITUDE));
        }
        
        if (!isValidLongitude(lon)) {
            if (errorMsg.length() > 0) {
                errorMsg.append("; ");
            }
            errorMsg.append(String.format("The longitude value %s is outside the valid range [%s, %s]",
                    lon, Validation.MIN_LONGITUDE, Validation.MAX_LONGITUDE));
        }
        
        if (errorMsg.length() > 0) {
            log.error("Invalid latitude and longitude: {}", errorMsg);
            throw new OpenWeatherException(errorMsg.toString(), ErrorCodes.INVALID_COORDINATES);
        }
    }

    private boolean isValidLatitude(double lat) {
        return lat >= Validation.MIN_LATITUDE && lat <= Validation.MAX_LATITUDE;
    }

    private boolean isValidLongitude(double lon) {
        return lon >= Validation.MIN_LONGITUDE && lon <= Validation.MAX_LONGITUDE;
    }

    public CountryInfo getCityInfo(String cityName, String countryCode) {
        String geoUrl = buildGeoUrl(cityName, countryCode);

        Map<String, Object>[] geoResponse = restTemplate.getForObject(geoUrl, Map[].class);
        if (geoResponse == null || geoResponse.length == 0) {
            return null;
        }
        Map<String, Object> geoData = geoResponse[0];
        double lat = (double) geoData.get("lat");
        double lon = (double) geoData.get("lon");
        String foundCityName = (String) geoData.get("name");
        String foundCountryCode = (String) geoData.get("country");

        return CountryInfo.builder()
                .name(foundCityName)
                .country(foundCountryCode)
                .lat(lat)
                .lon(lon)
                .build();
    }
}
