package cn.aetherial.entity;

import cn.aetherial.enums.MoonPhaseEnum;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Objects;

public class DailyWeather extends BaseWeather {

    private LocalDateTime sunrise;
    private LocalDateTime sunset;
    private LocalDateTime moonrise;
    private LocalDateTime moonset;
    private Double moonPhase;
    private String moonPhaseDescription;
    private String summary;
    private Temp temp;
    private FeelsLike feelsLike;
    private Double pop;
    private Double rain;
    private Double snow;
    private Weather weather;
    
    public DailyWeather() {
    }
    
    public DailyWeather(LocalDateTime sunrise, LocalDateTime sunset, LocalDateTime moonrise, LocalDateTime moonset,
                       Double moonPhase, String summary, Temp temp, FeelsLike feelsLike, Double pop,
                       Double rain, Double snow, Weather weather) {
        this.sunrise = sunrise;
        this.sunset = sunset;
        this.moonrise = moonrise;
        this.moonset = moonset;
        this.moonPhase = moonPhase;
        this.summary = summary;
        this.temp = temp;
        this.feelsLike = feelsLike;
        this.pop = pop;
        this.rain = rain;
        this.snow = snow;
        this.weather = weather;
        setMoonPhaseDescription();
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

    public LocalDateTime getMoonrise() {
        return moonrise;
    }

    public void setMoonrise(LocalDateTime moonrise) {
        this.moonrise = moonrise;
    }

    public void setMoonrise(Long timestamp) {
        if (timestamp != null) {
            this.moonrise = LocalDateTime.ofInstant(Instant.ofEpochSecond(timestamp), ZoneId.systemDefault());
        } else {
            this.moonrise = null;
        }
    }

    public LocalDateTime getMoonset() {
        return moonset;
    }

    public void setMoonset(LocalDateTime moonset) {
        this.moonset = moonset;
    }

    public void setMoonset(Long timestamp) {
        if (timestamp != null) {
            this.moonset = LocalDateTime.ofInstant(Instant.ofEpochSecond(timestamp), ZoneId.systemDefault());
        } else {
            this.moonset = null;
        }
    }

    public Double getMoonPhase() {
        return moonPhase;
    }

    public void setMoonPhase(Double moonPhase) {
        this.moonPhase = moonPhase;
        setMoonPhaseDescription();
    }

    public String getMoonPhaseDescription() {
        return moonPhaseDescription;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public Temp getTemp() {
        return temp;
    }

    public void setTemp(Temp temp) {
        this.temp = temp;
    }

    public FeelsLike getFeelsLike() {
        return feelsLike;
    }

    public void setFeelsLike(FeelsLike feelsLike) {
        this.feelsLike = feelsLike;
    }

    public Double getPop() {
        return pop;
    }

    public void setPop(Double pop) {
        this.pop = pop;
    }

    public Double getRain() {
        return rain;
    }

    public void setRain(Double rain) {
        this.rain = rain;
    }

    public Double getSnow() {
        return snow;
    }

    public void setSnow(Double snow) {
        this.snow = snow;
    }

    public Weather getWeather() {
        return weather;
    }

    public void setWeather(Weather weather) {
        this.weather = weather;
    }

    private void setMoonPhaseDescription() {
        if (this.moonPhase != null) {
            this.moonPhaseDescription = MoonPhaseEnum.getByPhase(this.moonPhase).getDescription();
        }
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        DailyWeather that = (DailyWeather) o;
        return Objects.equals(sunrise, that.sunrise) &&
               Objects.equals(sunset, that.sunset) &&
               Objects.equals(moonrise, that.moonrise) &&
               Objects.equals(moonset, that.moonset) &&
               Objects.equals(moonPhase, that.moonPhase) &&
               Objects.equals(moonPhaseDescription, that.moonPhaseDescription) &&
               Objects.equals(summary, that.summary) &&
               Objects.equals(temp, that.temp) &&
               Objects.equals(feelsLike, that.feelsLike) &&
               Objects.equals(pop, that.pop) &&
               Objects.equals(rain, that.rain) &&
               Objects.equals(snow, that.snow) &&
               Objects.equals(weather, that.weather);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), sunrise, sunset, moonrise, moonset, moonPhase,
                moonPhaseDescription, summary, temp, feelsLike, pop, rain, snow, weather);
    }

    @Override
    public String toString() {
        return "DailyWeather{" +
                "sunrise=" + sunrise +
                ", sunset=" + sunset +
                ", moonrise=" + moonrise +
                ", moonset=" + moonset +
                ", moonPhase=" + moonPhase +
                ", moonPhaseDescription='" + moonPhaseDescription + '\'' +
                ", summary='" + summary + '\'' +
                ", temp=" + temp +
                ", feelsLike=" + feelsLike +
                ", pop=" + pop +
                ", rain=" + rain +
                ", snow=" + snow +
                ", weather=" + weather +
                "} " + super.toString();
    }

}
