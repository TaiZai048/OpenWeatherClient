package cn.aetherial.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NoOpWeatherCache implements WeatherCache {
    
    private static final Logger log = LoggerFactory.getLogger(NoOpWeatherCache.class);
    
    public NoOpWeatherCache() {
    }
    
    @Override
    public Object get(String key) {
        return null;
    }
    
    @Override
    public void put(String key, Object value, int expireSeconds) {
    }
    
    @Override
    public void remove(String key) {
    }
    
    @Override
    public void clear() {
    }
    
    @Override
    public String getType() {
        return "none";
    }
}
