package ru.gelin.android.weather.google;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Locale;

import ru.gelin.android.weather.Location;
import ru.gelin.android.weather.Weather;
import ru.gelin.android.weather.WeatherException;
import ru.gelin.android.weather.WeatherSource;

/**
 *  Weather source which takes weather from the Google API.
 */
public class GoogleWeatherSource implements WeatherSource {

    /** API URL */
    static final String API_URL = "http://www.google.com/ig/api?weather=%s&hl=%s";
    
    @Override
    public Weather query(Location location) throws WeatherException {
        return query(location, Locale.getDefault());
    }

    @Override
    public Weather query(Location location, Locale locale)
            throws WeatherException {
        String fullUrl;
        try {
            fullUrl = String.format(API_URL, 
                    URLEncoder.encode(location.getQuery(), "UTF-8"),
                    URLEncoder.encode(locale.getLanguage(), "UTF-8"));
        } catch (UnsupportedEncodingException uee) {
            throw new RuntimeException(uee);    //should never happen
        }
        URL url;
        try {
            url = new URL(fullUrl);
        } catch (MalformedURLException mue) {
            throw new WeatherException("invalid URL: " + fullUrl, mue);
        }
        try {
            URLConnection connection = url.openConnection();
            connection.addRequestProperty("Accept-Charset", "UTF-8");
            return new GoogleWeather(connection.getInputStream());
        } catch (IOException ie) {
            throw new WeatherException("cannot read URL: " + fullUrl, ie);
        }
    }

}
