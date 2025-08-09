package cn.aetherial.entity;

public class WeatherSimple {

    private Double lat;
    private Double lon;
    private String timezone;
    private Integer timezoneOffset;
    private CurrentSimple current;
    
    public Double getLat() {
        return lat;
    }
    
    public void setLat(Double lat) {
        this.lat = lat;
    }
    
    public Double getLon() {
        return lon;
    }
    
    public void setLon(Double lon) {
        this.lon = lon;
    }
    
    public String getTimezone() {
        return timezone;
    }
    
    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }
    
    public Integer getTimezoneOffset() {
        return timezoneOffset;
    }
    
    public void setTimezoneOffset(Integer timezoneOffset) {
        this.timezoneOffset = timezoneOffset;
    }
    
    public CurrentSimple getCurrent() {
        return current;
    }
    
    public void setCurrent(CurrentSimple current) {
        this.current = current;
    }

    public static class CurrentSimple {

        private Double temp;
        private Double feelsLike;
        private Integer humidity;
        private Weather weather;
        
        public Double getTemp() {
            return temp;
        }
        
        public void setTemp(Double temp) {
            this.temp = temp;
        }
        
        public Double getFeelsLike() {
            return feelsLike;
        }
        
        public void setFeelsLike(Double feelsLike) {
            this.feelsLike = feelsLike;
        }
        
        public Integer getHumidity() {
            return humidity;
        }
        
        public void setHumidity(Integer humidity) {
            this.humidity = humidity;
        }
        
        public Weather getWeather() {
            return weather;
        }
        
        public void setWeather(Weather weather) {
            this.weather = weather;
        }
    }
}
