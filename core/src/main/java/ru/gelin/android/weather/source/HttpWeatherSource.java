/*
 * Copyright 2010—2016 Denis Nelubin and others.
 *
 * This file is part of Weather Notification.
 *
 * Weather Notification is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Weather Notification is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Weather Notification.  If not, see http://www.gnu.org/licenses/.
 */

package ru.gelin.android.weather.source;

import android.util.Log;
import ru.gelin.android.weather.WeatherException;
import ru.gelin.android.weather.notification.app.Tag;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 *  Abstract class for a weather source which uses HTTP for transport
 */
public class HttpWeatherSource {

    /** Main encoding */
    public static final String ENCODING = "UTF-8";

    /** User Agent of the Weather Source */
    static final String USER_AGENT = "Weather Notification (Linux; Android)";

    /** HTTP URL connection */
    private HttpURLConnection connection;

    /**
     *  Reads the content of the specified URL.
     */
    protected Reader getReaderForURL(String url) throws WeatherException {
        Log.d(Tag.TAG, "requesting " + url);
        try {
            connection = (HttpURLConnection) new URL(url).openConnection();
            connection.addRequestProperty("User-Agent", USER_AGENT);
            prepareConnection(connection);
        } catch (Exception e) {
            throw new WeatherException("Can't prepare http connection", e);
        }

        try {
            connection.connect();
            String encoding = connection.getContentEncoding();
            if (encoding == null) {
                encoding = ENCODING;
            }
            InputStream inputStream = connection.getInputStream();
            return new InputStreamReader(inputStream, encoding);
        } catch (Exception e) {
            throw new WeatherException("Problem communicating with API", e);
        }
    }

    /**
     *  Add necessary headers to the GET request.
     */
    protected void prepareConnection(HttpURLConnection connection) {
        //void implementation
    }

}
