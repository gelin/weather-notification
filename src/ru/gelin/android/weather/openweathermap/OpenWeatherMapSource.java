package ru.gelin.android.weather.openweathermap;

import org.json.JSONObject;
import ru.gelin.android.weather.Location;
import ru.gelin.android.weather.Weather;
import ru.gelin.android.weather.WeatherException;
import ru.gelin.android.weather.WeatherSource;

import java.util.Locale;

/**
 *  Weather source implementation which uses openweathermap.org
 */
public class OpenWeatherMapSource implements WeatherSource {

    /** API URL */
    static final String API_URL = "http://openweathermap.org/data/getrect?";

    JSONObject json;

    @Override
    public Weather query(Location location) throws WeatherException {
        return null;
    }

    @Override
    public Weather query(Location location, Locale locale) throws WeatherException {
        return query(location);
        //TODO: what to do with locale?
    }

}
