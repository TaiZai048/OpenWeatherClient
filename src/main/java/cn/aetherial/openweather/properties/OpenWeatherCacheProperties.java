package cn.aetherial.openweather.properties;

import cn.aetherial.openweather.exception.OpenWeatherException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.util.StringUtils;

@ConfigurationProperties(prefix = "open-weather-config.cache")
public class OpenWeatherCacheProperties implements InitializingBean {

    private boolean enabled = false;
    private String type = "auto";
    private int expireTime = 600;
    private int maxSize = 1000;
    private String keyPrefix = "openweather:";

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(int expireTime) {
        this.expireTime = expireTime;
    }

    public int getMaxSize() {
        return maxSize;
    }

    public void setMaxSize(int maxSize) {
        this.maxSize = maxSize;
    }

    public String getKeyPrefix() {
        return keyPrefix;
    }

    public void setKeyPrefix(String keyPrefix) {
        this.keyPrefix = keyPrefix;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (enabled) {
            if (!StringUtils.hasText(type) ||
                    (!"auto".equalsIgnoreCase(type) &&
                            !"redis".equalsIgnoreCase(type) &&
                            !"local".equalsIgnoreCase(type))) {
                throw new OpenWeatherException("Invalid cache type configuration: " + type + ". Allowed values are auto, redis, local",
                        OpenWeatherException.ErrorCodes.INVALID_CACHE_TYPE
                );
            }
        }
    }
}
