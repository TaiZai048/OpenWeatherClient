package cn.aetherial.entity;

import java.util.Objects;

public class Temp {

    private Double morn;
    private Double day;
    private Double eve;
    private Double night;
    private Double min;
    private Double max;

    public Temp() {
    }

    public Temp(Double morn, Double day, Double eve, Double night, Double min, Double max) {
        this.morn = morn;
        this.day = day;
        this.eve = eve;
        this.night = night;
        this.min = min;
        this.max = max;
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

    public Double getMin() {
        return min;
    }

    public void setMin(Double min) {
        this.min = min;
    }

    public Double getMax() {
        return max;
    }

    public void setMax(Double max) {
        this.max = max;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Temp temp = (Temp) o;
        return Objects.equals(morn, temp.morn) &&
               Objects.equals(day, temp.day) &&
               Objects.equals(eve, temp.eve) &&
               Objects.equals(night, temp.night) &&
               Objects.equals(min, temp.min) &&
               Objects.equals(max, temp.max);
    }

    @Override
    public int hashCode() {
        return Objects.hash(morn, day, eve, night, min, max);
    }

    @Override
    public String toString() {
        return "Temp{" +
                "morn=" + morn +
                ", day=" + day +
                ", eve=" + eve +
                ", night=" + night +
                ", min=" + min +
                ", max=" + max +
                '}';
    }
}
