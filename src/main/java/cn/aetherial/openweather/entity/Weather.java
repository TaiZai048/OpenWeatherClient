package cn.aetherial.openweather.entity;

import java.util.Objects;

public class Weather {

    private Integer id;
    private String main;
    private String description;
    private String icon;
    private String iconUrl;
    
    public Weather() {
    }
    
    public Weather(Integer id, String main, String description, String icon) {
        this.id = id;
        this.main = main;
        this.description = description;
        this.icon = icon;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMain() {
        return main;
    }

    public void setMain(String main) {
        this.main = main;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
        this.setIconUrl();
    }

    private void setIconUrl() {
        if (icon != null && !icon.isEmpty()) {
            this.iconUrl = "https://openweathermap.org/img/wn/" + icon + "@2x.png";
        }
    }

    public String getIconUrl() {
        return this.iconUrl;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Weather weather = (Weather) o;
        return Objects.equals(id, weather.id) &&
               Objects.equals(main, weather.main) &&
               Objects.equals(description, weather.description) &&
               Objects.equals(icon, weather.icon);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, main, description, icon);
    }

    @Override
    public String toString() {
        return "Weather{" +
                "id=" + id +
                ", main='" + main + '\'' +
                ", description='" + description + '\'' +
                ", icon='" + icon + '\'' +
                '}';
    }

}
