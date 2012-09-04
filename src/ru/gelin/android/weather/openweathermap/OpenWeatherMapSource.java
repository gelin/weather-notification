package ru.gelin.android.weather.openweathermap;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import ru.gelin.android.weather.Location;
import ru.gelin.android.weather.Weather;
import ru.gelin.android.weather.WeatherException;
import ru.gelin.android.weather.WeatherSource;
import ru.gelin.android.weather.source.HttpWeatherSource;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Locale;

/**
 *  Weather source implementation which uses openweathermap.org
 */
public class OpenWeatherMapSource extends HttpWeatherSource implements WeatherSource {

    /** Base API URL */
    static final String API_BASE_URL = "http://openweathermap.org/data/2.0";
    /** Find city API URL */
    static final String API_CITY_URL = API_BASE_URL + "/find/city?";
    /** Find by name API URL */
    static final String API_NAME_URL = API_BASE_URL + "/find/name?";
    /** Forecasts API URL */
    static final String API_FORECAST_URL = API_BASE_URL + "/forecast/city/";

    @Override
    public Weather query(Location location) throws WeatherException {
        OpenWeatherMapWeather weather = new OpenWeatherMapWeather();
        weather.parseCityWeather(queryCityWeather(location));
        if (weather.isEmpty()) {
            return weather;
        }
        weather.parseForecast(queryForecast(weather.getCityId()));
        return weather;
    }

    @Override
    public Weather query(Location location, Locale locale) throws WeatherException {
        return query(location);
        //TODO: what to do with locale?
    }

    JSONObject queryCityWeather(Location location) throws WeatherException {
        String url;
        if (location.isGeo()) {
            url = API_CITY_URL + location.getQuery();
        } else {
            url = API_NAME_URL + location.getQuery();
        }
        JSONTokener parser = new JSONTokener(readJSON(url));
        try {
            return (JSONObject)parser.nextValue();
        } catch (JSONException e) {
            throw new WeatherException("can't parse weather", e);
        }
    }

    JSONObject queryForecast(int cityId) throws WeatherException {
        String url = API_FORECAST_URL + String.valueOf(cityId);
        JSONTokener parser = new JSONTokener(readJSON(url));
        try {
            return (JSONObject)parser.nextValue();
        } catch (JSONException e) {
            throw new WeatherException("can't parse forecast", e);
        }
    }

    String readJSON(String url) throws WeatherException {
        StringBuilder result = new StringBuilder();
        InputStreamReader reader = getReaderForURL(url);
        char[] buf = new char[1024];
        try {
            int read = reader.read(buf);
            while (read >= 0) {
                result.append(buf, 0 , read);
                read = reader.read(buf);
            }
        } catch (IOException e) {
            throw new WeatherException("can't read weather", e);
        }
        return result.toString();
    }

}
