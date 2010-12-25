package ru.gelin.android.weather;

/**
 *  Common exception for weather getting errors.
 */
public class WeatherException extends Exception {

    public WeatherException() {
        super();
    }

    public WeatherException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public WeatherException(String detailMessage) {
        super(detailMessage);
    }

    public WeatherException(Throwable throwable) {
        super(throwable);
    }

}
