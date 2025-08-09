package cn.aetherial.service;

import cn.aetherial.cache.WeatherCache;
import cn.aetherial.config.WeatherDetailsConfig;
import cn.aetherial.constant.WeatherConstants.Validation;
import cn.aetherial.entity.CountryInfo;
import cn.aetherial.entity.WeatherDetails;
import cn.aetherial.entity.WeatherSimple;
import cn.aetherial.entity.WeatherStandard;
import cn.aetherial.enums.WeatherDetailLevel;
import cn.aetherial.exception.OpenWeatherException;
import cn.aetherial.exception.OpenWeatherException.ErrorCodes;
import cn.aetherial.factory.WeatherDataStrategyFactory;
import cn.aetherial.properties.OpenWeatherProperties;
import cn.aetherial.strategy.DetailedWeatherStrategy;
import cn.aetherial.strategy.WeatherDataStrategy;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.client.RestClientException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.concurrent.ConcurrentHashMap;

import java.time.Duration;

import java.util.Map;

import static cn.aetherial.constant.WeatherConstants.Api.*;

@Service
public class OpenWeatherService {

    private static final Logger log = LoggerFactory.getLogger(OpenWeatherService.class);
    
    private final RestTemplate restTemplate;
    private final OpenWeatherProperties properties;
    private final WeatherDataStrategyFactory strategyFactory;
    private final WeatherCache weatherCache;
    private final ObjectMapper objectMapper;
    private final Map<String, Object> keyLocks = new ConcurrentHashMap<>();

    @Autowired
    public OpenWeatherService(RestTemplateBuilder restTemplateBuilder, OpenWeatherProperties properties, 
                             WeatherDataStrategyFactory strategyFactory, WeatherCache weatherCache, ObjectMapper objectMapper) {
        this.properties = properties;
        this.strategyFactory = strategyFactory;
        this.weatherCache = weatherCache;
        this.objectMapper = objectMapper;
        this.restTemplate = restTemplateBuilder
                .setConnectTimeout(Duration.ofMillis(properties.getConnectionTimeout()))
                .setReadTimeout(Duration.ofMillis(properties.getReadTimeout()))
                .build();
        
        log.info("OpenWeatherService initialization is complete, using cache type: {}", weatherCache.getType());
    }

    private String buildWeatherUrl(double lat, double lon) {
        return UriComponentsBuilder.fromHttpUrl(BASE_URL)
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
        return UriComponentsBuilder.fromHttpUrl(GEOCODING_URL)
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
