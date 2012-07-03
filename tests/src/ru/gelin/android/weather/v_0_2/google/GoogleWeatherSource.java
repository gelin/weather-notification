/*
 *  Weather API.
 *  Copyright (C) 2010  Denis Nelubin aka Gelin
 *
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 *
 *  http://gelin.ru
 *  mailto:den@gelin.ru
 */

package ru.gelin.android.weather.v_0_2.google;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Locale;

import ru.gelin.android.weather.v_0_2.Location;
import ru.gelin.android.weather.v_0_2.Weather;
import ru.gelin.android.weather.v_0_2.WeatherException;
import ru.gelin.android.weather.v_0_2.WeatherSource;

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
    
    //@Override
    public Weather query(Location location) throws WeatherException {
        return query(location, Locale.getDefault());
    }

    //@Override
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
            GoogleWeather weather = new GoogleWeather(new InputStreamReader(
                    connection.getInputStream(), charset));
            if (weather.getLocation().isEmpty()) {
                weather.setLocation(location);  //set original location
            }
            return weather; 
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
