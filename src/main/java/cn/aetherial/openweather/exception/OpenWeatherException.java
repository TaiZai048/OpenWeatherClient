package cn.aetherial.openweather.exception;

public class OpenWeatherException extends RuntimeException {
    
    private final String errorCode;
    
    public OpenWeatherException(String message) {
        this(message, "WEATHER_ERROR");
    }
    
    public OpenWeatherException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
    
    public OpenWeatherException(String message, String errorCode, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }
    
    public String getErrorCode() {
        return errorCode;
    }
    
    public static class ErrorCodes {
        public static final String INVALID_COORDINATES = "INVALID_COORDINATES";
        public static final String API_REQUEST_FAILED = "API_REQUEST_FAILED";
        public static final String API_KEY_NOT_SET = "API_KEY_NOT_SET";
        public static final String INVALID_CACHE_TYPE = "INVALID_CACHE_TYPE";
    }
}
