/*
 *  Weather API.
 *  Copyright (C) 2010  Denis Nelubin, Vladimir Kubyshev
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

package ru.gelin.android.weather.google;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Locale;

import javax.xml.parsers.ParserConfigurationException;

import ru.gelin.android.weather.Location;
import ru.gelin.android.weather.Weather;
import ru.gelin.android.weather.WeatherException;
import ru.gelin.android.weather.WeatherSource;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

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

    private static final int HTTP_STATUS_OK = 200;

    static final String USER_AGENT = "Google Weather/1.0 (Linux; Android)";

    GoogleWeatherParser weatherParser;
    GoogleWeather weather;
    HttpClient client;
    HttpGet request;
    //@Override
    public Weather query(Location location) throws WeatherException {
        return query(location, Locale.getDefault());
    }

    InputStreamReader getReaderForURL(String URL) throws WeatherException {
        try {
            request = new HttpGet(URL);
            request.setHeader("User-Agent", USER_AGENT);
        } catch (Exception e) {
            throw new WeatherException("Can't prepare http request", e);
        }

        String charset = ENCODING;
        try {
            HttpResponse response = client.execute(request);

            StatusLine status = response.getStatusLine();
            if (status.getStatusCode() != HTTP_STATUS_OK) {
                throw new WeatherException("Invalid response from server: " +
                        status.toString());
            }

            HttpEntity entity = response.getEntity();
            charset = getCharset(entity);
            InputStreamReader inputStream = new InputStreamReader(entity.getContent(), charset);

            return inputStream;
        } catch (UnsupportedEncodingException uee) {
            throw new WeatherException("unsupported charset: " + charset, uee);
        } catch (IOException e) {
            throw new WeatherException("Problem communicating with API", e);
        }
    }

    void parseContent(Location location, String locale, DefaultHandler handler) 
            throws WeatherException, SAXException, ParserConfigurationException, IOException {
        String fullUrl;
        try {
            fullUrl = String.format(API_URL, 
                    URLEncoder.encode(location.getQuery(), ENCODING),
                    URLEncoder.encode(locale, ENCODING));
        } catch (UnsupportedEncodingException uee) {
            throw new RuntimeException(uee);    //should never happen
        }

        InputStreamReader reader = getReaderForURL(fullUrl);
        GoogleWeatherParser weatherParser = new GoogleWeatherParser(weather);
        weatherParser.parse(reader, handler);
    }
    //@Override
    public Weather query(Location location, Locale locale)
            throws WeatherException {

        // Create client and set our specific user-agent string
        try {
            client = new DefaultHttpClient();
        } catch (Exception e) {
            throw new WeatherException("Can't prepare http client", e);
        }

        try {
            weather = new GoogleWeather();
            weatherParser = new GoogleWeatherParser(weather);
            parseContent(location, "us", new EnglishParserHandler(weather));
            parseContent(location, locale.getLanguage(), new ParserHandler(weather));

            if (weather.getLocation().isEmpty()) {
                weather.setLocation(location);  //set original location
            }
            return weather;
        } catch (Exception e) {
            throw new WeatherException("create weather error", e);
        }
    }

    static String getCharset(HttpEntity entity) {
        return getCharset(entity.getContentType().toString());
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
