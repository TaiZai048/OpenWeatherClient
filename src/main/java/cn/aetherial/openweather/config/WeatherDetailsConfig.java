package cn.aetherial.openweather.config;

public class WeatherDetailsConfig {

    private boolean includeMinutely = true;
    private boolean includeHourly = true;
    private boolean includeDaily = true;
    private boolean includeAlerts = true;

    private WeatherDetailsConfig () {
    }

    public static WeatherDetailsConfig createDefault() {
        return new WeatherDetailsConfig();
    }

    public static WeatherDetailsConfig create(boolean includeMinutely, boolean includeHourly, 
                                             boolean includeDaily, boolean includeAlerts) {
        WeatherDetailsConfig config = new WeatherDetailsConfig();
        config.setIncludeMinutely(includeMinutely);
        config.setIncludeHourly(includeHourly);
        config.setIncludeDaily(includeDaily);
        config.setIncludeAlerts(includeAlerts);
        return config;
    }

    public boolean isIncludeMinutely() {
        return includeMinutely;
    }

    public void setIncludeMinutely(boolean includeMinutely) {
        this.includeMinutely = includeMinutely;
    }

    public boolean isIncludeHourly() {
        return includeHourly;
    }

    public void setIncludeHourly(boolean includeHourly) {
        this.includeHourly = includeHourly;
    }

    public boolean isIncludeDaily() {
        return includeDaily;
    }

    public void setIncludeDaily(boolean includeDaily) {
        this.includeDaily = includeDaily;
    }

    public boolean isIncludeAlerts() {
        return includeAlerts;
    }

    public void setIncludeAlerts(boolean includeAlerts) {
        this.includeAlerts = includeAlerts;
    }
}
