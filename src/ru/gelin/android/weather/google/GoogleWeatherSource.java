package ru.gelin.android.weather.google;

import java.io.IOException;
import java.io.InputStreamReader;
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
    /** Main encoding */
    static final String ENCODING = "UTF-8";
    /** Charset pattern */
    static final String CHARSET = "charset=";
    
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
                    URLEncoder.encode(location.getQuery(), ENCODING),
                    URLEncoder.encode(locale.getLanguage(), ENCODING));
        } catch (UnsupportedEncodingException uee) {
            throw new RuntimeException(uee);    //should never happen
        }
        URL url;
        try {
            url = new URL(fullUrl);
        } catch (MalformedURLException mue) {
            throw new WeatherException("invalid URL: " + fullUrl, mue);
        }
        String charset = ENCODING;
        try {
            URLConnection connection = url.openConnection();
            //connection.addRequestProperty("Accept-Charset", "UTF-8");
            //connection.addRequestProperty("Accept-Language", locale.getLanguage());
            charset = getCharset(connection);
            return new GoogleWeather(new InputStreamReader(
                    connection.getInputStream(), charset));
        } catch (UnsupportedEncodingException uee) {
            throw new WeatherException("unsupported charset: " + charset, uee);
        } catch (IOException ie) {
            throw new WeatherException("cannot read URL: " + fullUrl, ie);
        }
    }
    
    static String getCharset(URLConnection connection) {
        return getCharset(connection.getContentType());
    }
    
    static String getCharset(String contentType) {
        if (contentType == null) {
            return ENCODING;
        }
        int charsetPos = contentType.indexOf(CHARSET);
        if (charsetPos < 0) {
            return ENCODING;
        }
        charsetPos += CHARSET.length();
        int endPos = contentType.indexOf(';', charsetPos);
        if (endPos < 0) {
            endPos = contentType.length();
        }
        return contentType.substring(charsetPos, endPos);
    }

}
