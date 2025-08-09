package cn.aetherial.entity;

import java.util.Objects;

public class HourlyWeather extends BaseWeather {

    private Double pop;
    private Weather weather;
    private Double temp;
    private Double feelsLike;
    
    public HourlyWeather() {
    }
    
    public HourlyWeather(Double pop, Weather weather, Double temp, Double feelsLike) {
        this.pop = pop;
        this.weather = weather;
        this.temp = temp;
        this.feelsLike = feelsLike;
    }

    public Double getPop() {
        return pop;
    }

    public void setPop(Double pop) {
        this.pop = pop;
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
        HourlyWeather that = (HourlyWeather) o;
        return Objects.equals(pop, that.pop) &&
               Objects.equals(weather, that.weather) &&
               Objects.equals(temp, that.temp) &&
               Objects.equals(feelsLike, that.feelsLike);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), pop, weather, temp, feelsLike);
    }

    @Override
    public String toString() {
        return "HourlyWeather{" +
                "pop=" + pop +
                ", weather=" + weather +
                ", temp=" + temp +
                ", feelsLike=" + feelsLike +
                "} " + super.toString();
    }
}
