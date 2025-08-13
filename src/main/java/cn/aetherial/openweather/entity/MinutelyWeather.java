package cn.aetherial.openweather.entity;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Objects;

public class MinutelyWeather {

    private LocalDateTime dt;
    private Double precipitation;
    
    public MinutelyWeather() {
    }
    
    public MinutelyWeather(LocalDateTime dt, Double precipitation) {
        this.dt = dt;
        this.precipitation = precipitation;
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

    public Double getPrecipitation() {
        return precipitation;
    }

    public void setPrecipitation(Double precipitation) {
        this.precipitation = precipitation;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MinutelyWeather that = (MinutelyWeather) o;
        return Objects.equals(dt, that.dt) &&
               Objects.equals(precipitation, that.precipitation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dt, precipitation);
    }

    @Override
    public String toString() {
        return "MinutelyWeather{" +
                "dt=" + dt +
                ", precipitation=" + precipitation +
                '}';
    }
}
