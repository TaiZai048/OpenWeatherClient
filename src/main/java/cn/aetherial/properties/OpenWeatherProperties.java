package cn.aetherial.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.util.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import cn.aetherial.exception.OpenWeatherException;

@ConfigurationProperties(prefix = "open-weather-config")
public class OpenWeatherProperties implements InitializingBean {

    private String apiKey;
    private String units = "metric";
    private String lang = "zh_cn";
    private int connectionTimeout = 5000;
    private int readTimeout = 5000;

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getUnits() {
        return units;
    }

    public void setUnits(String units) {
        this.units = units;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }
    
    public int getConnectionTimeout() {
        return connectionTimeout;
    }
    
    public void setConnectionTimeout(int connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }
    
    public int getReadTimeout() {
        return readTimeout;
    }
    
    public void setReadTimeout(int readTimeout) {
        this.readTimeout = readTimeout;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (!StringUtils.hasText(apiKey)) {
            throw new OpenWeatherException(
                "Error: OpenWeather API Key not set. Please set the 'open-weather-config.api-key' property in the configuration file.",
                OpenWeatherException.ErrorCodes.API_KEY_NOT_SET
            );
        }
    }
}
