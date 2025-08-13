package cn.aetherial.openweather.cache;

public interface WeatherCache {

    Object get(String key);

    void put(String key, Object value, int expireSeconds);

    void remove(String key);

    void clear();

    String getType();
}
