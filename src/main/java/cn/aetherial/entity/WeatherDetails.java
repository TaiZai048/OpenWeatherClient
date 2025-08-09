package cn.aetherial.entity;

import java.util.List;
import java.util.Objects;

public class WeatherDetails {

    private Double lat;
    private Double lon;
    private String timezone;
    private Integer timezoneOffset;
    private CurrentWeather current;
    private List<MinutelyWeather> minutely;
    private List<HourlyWeather> hourly;
    private List<DailyWeather> daily;
    private Alerts alert;
    
    public WeatherDetails() {
    }
    
    public WeatherDetails(Double lat, Double lon, String timezone, Integer timezoneOffset,
                         CurrentWeather current, List<MinutelyWeather> minutely, List<HourlyWeather> hourly,
                          List<DailyWeather> daily, Alerts alert) {
        this.lat = lat;
        this.lon = lon;
        this.timezone = timezone;
        this.timezoneOffset = timezoneOffset;
        this.current = current;
        this.minutely = minutely;
        this.hourly = hourly;
        this.daily = daily;
        this.alert = alert;
    }

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

    public List<MinutelyWeather> getMinutely() {
        return minutely;
    }

    public void setMinutely(List<MinutelyWeather> minutely) {
        this.minutely = minutely;
    }

    public List<HourlyWeather> getHourly() {
        return hourly;
    }

    public void setHourly(List<HourlyWeather> hourly) {
        this.hourly = hourly;
    }

    public List<DailyWeather> getDaily() {
        return daily;
    }

    public void setDaily(List<DailyWeather> daily) {
        this.daily = daily;
    }

    public Alerts getAlert() {
        return alert;
    }

    public void setAlert(Alerts alert) {
        this.alert = alert;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WeatherDetails that = (WeatherDetails) o;
        return Objects.equals(lat, that.lat) &&
               Objects.equals(lon, that.lon) &&
               Objects.equals(timezone, that.timezone) &&
               Objects.equals(timezoneOffset, that.timezoneOffset) &&
               Objects.equals(current, that.current) &&
               Objects.equals(minutely, that.minutely) &&
               Objects.equals(hourly, that.hourly) &&
               Objects.equals(daily, that.daily) &&
               Objects.equals(alert, that.alert);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lat, lon, timezone, timezoneOffset, current, minutely, hourly, daily, alert);
    }

    @Override
    public String toString() {
        return "WeatherDetails{" +
                "lat=" + lat +
                ", lon=" + lon +
                ", timezone='" + timezone + '\'' +
                ", timezoneOffset=" + timezoneOffset +
                ", current=" + current +
                ", minutely=" + minutely +
                ", hourly=" + hourly +
                ", daily=" + daily +
                ", alert=" + alert +
                '}';
    }

}
