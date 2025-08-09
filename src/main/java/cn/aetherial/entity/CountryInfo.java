package cn.aetherial.entity;

import java.util.Objects;

public class CountryInfo {

    private String name;
    private Double lat;
    private Double lon;
    private String country;
    private String state;

    private CountryInfo() {}

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private final CountryInfo countryInfo;
        
        private Builder() {
            countryInfo = new CountryInfo();
        }
        
        public Builder name(String name) {
            countryInfo.name = name;
            return this;
        }
        
        public Builder lat(Double lat) {
            countryInfo.lat = lat;
            return this;
        }
        
        public Builder lon(Double lon) {
            countryInfo.lon = lon;
            return this;
        }
        
        public Builder country(String country) {
            countryInfo.country = country;
            return this;
        }
        
        public Builder state(String state) {
            countryInfo.state = state;
            return this;
        }
        
        public CountryInfo build() {
            return countryInfo;
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        CountryInfo that = (CountryInfo) o;
        return Objects.equals(name, that.name) &&
               Objects.equals(lat, that.lat) && 
               Objects.equals(lon, that.lon) && 
               Objects.equals(country, that.country) && 
               Objects.equals(state, that.state);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, lat, lon, country, state);
    }

    @Override
    public String toString() {
        return "CountryInfo{" +
                "name='" + name + '\'' +
                ", lat=" + lat +
                ", lon=" + lon +
                ", country='" + country + '\'' +
                ", state='" + state + '\'' +
                '}';
    }
}
