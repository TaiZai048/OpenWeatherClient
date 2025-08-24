package cn.aetherial.openweather.constant;

public final class WeatherConstants {
    
    private WeatherConstants() {
    }
    
    public static final class Api {
        public static final String DEFAULT_DOMAIN = "https://api.openweathermap.org";
        public static final String WEATHER_PATH = "/data/3.0/onecall";
        public static final String GEOCODING_PATH = "/geo/1.0/direct";

        public static final String PARAM_LAT = "lat";
        public static final String PARAM_LON = "lon";
        public static final String PARAM_API_KEY = "appid";
        public static final String PARAM_UNITS = "units";
        public static final String PARAM_LANG = "lang";
        public static final String PARAM_CITY = "q";
        public static final String PARAM_LIMIT = "limit";
    }
    
    public static final class Validation {
        public static final double MIN_LATITUDE = -90.0;
        public static final double MAX_LATITUDE = 90.0;
        public static final double MIN_LONGITUDE = -180.0;
        public static final double MAX_LONGITUDE = 180.0;
    }
}
