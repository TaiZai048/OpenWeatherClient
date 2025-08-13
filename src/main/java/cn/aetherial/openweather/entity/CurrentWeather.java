package cn.aetherial.openweather.entity;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Objects;

public class CurrentWeather extends BaseWeather {


    private LocalDateTime sunrise;
    private LocalDateTime sunset;
    private Weather weather;
    private Double temp;
    private Double feelsLike;
    
    public CurrentWeather() {
    }
    
    public CurrentWeather(LocalDateTime sunrise, LocalDateTime sunset, Weather weather, Double temp, Double feelsLike) {
        this.sunrise = sunrise;
        this.sunset = sunset;
        this.weather = weather;
        this.temp = temp;
        this.feelsLike = feelsLike;
    }

    public LocalDateTime getSunrise() {
        return sunrise;
    }

    public void setSunrise(LocalDateTime sunrise) {
        this.sunrise = sunrise;
    }

    public void setSunrise(Long timestamp) {
        if (timestamp != null) {
            this.sunrise = LocalDateTime.ofInstant(Instant.ofEpochSecond(timestamp), ZoneId.systemDefault());
        } else {
            this.sunrise = null;
        }
    }

    public LocalDateTime getSunset() {
        return sunset;
    }

    public void setSunset(LocalDateTime sunset) {
        this.sunset = sunset;
    }

    public void setSunset(Long timestamp) {
        if (timestamp != null) {
            this.sunset = LocalDateTime.ofInstant(Instant.ofEpochSecond(timestamp), ZoneId.systemDefault());
        } else {
            this.sunset = null;
        }
    }

    public Weather getWeather() {
        return weather;
    }

    public void setWeather(Weather weather) {
        this.weather = weather;
    }

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
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        CurrentWeather that = (CurrentWeather) o;
        return Objects.equals(sunrise, that.sunrise) &&
               Objects.equals(sunset, that.sunset) &&
               Objects.equals(weather, that.weather) &&
               Objects.equals(temp, that.temp) &&
               Objects.equals(feelsLike, that.feelsLike);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), sunrise, sunset, weather, temp, feelsLike);
    }

    @Override
    public String toString() {
        return "CurrentWeather{" +
                "sunrise=" + sunrise +
                ", sunset=" + sunset +
                ", weather=" + weather +
                ", temp=" + temp +
                ", feelsLike=" + feelsLike +
                "} " + super.toString();
    }

}
