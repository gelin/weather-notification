package ru.gelin.android.weather.openweathermap;

import ru.gelin.android.weather.WeatherException;

/**
 * Is thrown when the API returns 429 Too Many Requests.
 * The detailMessage contains the error message from API.
 */
public class TooManyRequestsException extends WeatherException {

    public TooManyRequestsException() {
        super();
    }

    public TooManyRequestsException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public TooManyRequestsException(String detailMessage) {
        super(detailMessage);
    }

    public TooManyRequestsException(Throwable throwable) {
        super(throwable);
    }

}
