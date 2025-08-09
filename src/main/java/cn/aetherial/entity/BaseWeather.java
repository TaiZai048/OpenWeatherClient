package cn.aetherial.entity;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Objects;

public class BaseWeather {

    private LocalDateTime dt;
    private Integer pressure;
    private Integer humidity;
    private Double dewPoint;
    private Integer clouds;
    private Double uvi;
    private Integer visibility;
    private Double windSpeed;
    private Double windGust;
    private Integer windDeg;
    
    public BaseWeather() {
    }
    
    public BaseWeather(LocalDateTime dt, Integer pressure, Integer humidity, Double dewPoint, Integer clouds, 
                      Double uvi, Integer visibility, Double windSpeed, Double windGust, Integer windDeg) {
        this.dt = dt;
        this.pressure = pressure;
        this.humidity = humidity;
        this.dewPoint = dewPoint;
        this.clouds = clouds;
        this.uvi = uvi;
        this.visibility = visibility;
        this.windSpeed = windSpeed;
        this.windGust = windGust;
        this.windDeg = windDeg;
    }

    public LocalDateTime getDt() {
        return dt;
    }

    public void setDt(LocalDateTime dt) {
        this.dt = dt;
    }

    public void setDt(Long timestamp) {
        if (timestamp != null) {
            this.dt = LocalDateTime.ofInstant(Instant.ofEpochSecond(timestamp), ZoneId.systemDefault());
        } else {
            this.dt = null;
        }
    }

    public Integer getPressure() {
        return pressure;
    }

    public void setPressure(Integer pressure) {
        this.pressure = pressure;
    }

    public Integer getHumidity() {
        return humidity;
    }

    public void setHumidity(Integer humidity) {
        this.humidity = humidity;
    }

    public Double getDewPoint() {
        return dewPoint;
    }

    public void setDewPoint(Double dewPoint) {
        this.dewPoint = dewPoint;
    }

    public Integer getClouds() {
        return clouds;
    }

    public void setClouds(Integer clouds) {
        this.clouds = clouds;
    }

    public Double getUvi() {
        return uvi;
    }

    public void setUvi(Double uvi) {
        this.uvi = uvi;
    }

    public Integer getVisibility() {
        return visibility;
    }

    public void setVisibility(Integer visibility) {
        this.visibility = visibility;
    }

    public Double getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(Double windSpeed) {
        this.windSpeed = windSpeed;
    }

    public Double getWindGust() {
        return windGust;
    }

    public void setWindGust(Double windGust) {
        this.windGust = windGust;
    }

    public Integer getWindDeg() {
        return windDeg;
    }

    public void setWindDeg(Integer windDeg) {
        this.windDeg = windDeg;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BaseWeather that = (BaseWeather) o;
        return Objects.equals(dt, that.dt) &&
               Objects.equals(pressure, that.pressure) &&
               Objects.equals(humidity, that.humidity) &&
               Objects.equals(dewPoint, that.dewPoint) &&
               Objects.equals(clouds, that.clouds) &&
               Objects.equals(uvi, that.uvi) &&
               Objects.equals(visibility, that.visibility) &&
               Objects.equals(windSpeed, that.windSpeed) &&
               Objects.equals(windGust, that.windGust) &&
               Objects.equals(windDeg, that.windDeg);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dt, pressure, humidity, dewPoint, clouds, uvi, visibility, windSpeed, windGust, windDeg);
    }

    @Override
    public String toString() {
        return "BaseWeather{" +
                "dt=" + dt +
                ", pressure=" + pressure +
                ", humidity=" + humidity +
                ", dewPoint=" + dewPoint +
                ", clouds=" + clouds +
                ", uvi=" + uvi +
                ", visibility=" + visibility +
                ", windSpeed=" + windSpeed +
                ", windGust=" + windGust +
                ", windDeg=" + windDeg +
                '}';
    }
}
