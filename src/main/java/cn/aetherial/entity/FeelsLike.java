package cn.aetherial.entity;

import java.util.Objects;

public class FeelsLike {

    private Double morn;
    private Double day;
    private Double eve;
    private Double night;

    public FeelsLike() {
    }

    public FeelsLike(Double morn, Double day, Double eve, Double night) {
        this.morn = morn;
        this.day = day;
        this.eve = eve;
        this.night = night;
    }

    public Double getMorn() {
        return morn;
    }

    public void setMorn(Double morn) {
        this.morn = morn;
    }

    public Double getDay() {
        return day;
    }

    public void setDay(Double day) {
        this.day = day;
    }

    public Double getEve() {
        return eve;
    }

    public void setEve(Double eve) {
        this.eve = eve;
    }

    public Double getNight() {
        return night;
    }

    public void setNight(Double night) {
        this.night = night;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FeelsLike feelsLike = (FeelsLike) o;
        return Objects.equals(morn, feelsLike.morn) &&
               Objects.equals(day, feelsLike.day) &&
               Objects.equals(eve, feelsLike.eve) &&
               Objects.equals(night, feelsLike.night);
    }

    @Override
    public int hashCode() {
        return Objects.hash(morn, day, eve, night);
    }

    @Override
    public String toString() {
        return "FeelsLike{" +
                "morn=" + morn +
                ", day=" + day +
                ", eve=" + eve +
                ", night=" + night +
                '}';
    }
}
