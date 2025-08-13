package cn.aetherial.openweather.entity;

public class WeatherStandard {

    private Double lat;
    private Double lon;
    private String timezone;
    private Integer timezoneOffset;
    private CurrentWeather current;
    
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
    
    public CurrentWeather getCurrent() {
        return current;
    }
    
    public void setCurrent(CurrentWeather current) {
        this.current = current;
    }
}
