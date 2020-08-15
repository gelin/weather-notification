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

package ru.gelin.android.weather.openweathermap;

import android.content.Context;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import ru.gelin.android.weather.Location;
import ru.gelin.android.weather.TestWeather;
import ru.gelin.android.weather.Weather;
import ru.gelin.android.weather.WeatherException;
import ru.gelin.android.weather.WeatherSource;
import ru.gelin.android.weather.notification.R;
import ru.gelin.android.weather.source.DebugDumper;
import ru.gelin.android.weather.source.HttpWeatherSource;

import java.io.IOException;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Locale;

/**
 *  Weather source implementation which uses openweathermap.org
 */
public class OpenWeatherMapSource extends HttpWeatherSource implements WeatherSource {

    private final Context context;
    private final String apiBaseUrl;
    private final DebugDumper debugDumper;
    private final OpenWeatherMapApiKey key;

    public OpenWeatherMapSource(Context context) {
        this.context = context;
        this.apiBaseUrl = context.getString(R.string.openweathermap_api_base_url);
        this.debugDumper = new DebugDumper(context, apiBaseUrl);
        this.key = new OpenWeatherMapApiKey(context);
    }

    @Override
    public Weather query(Location location) throws WeatherException {
        if (location == null) {
            throw new WeatherException("null location");
        }

        if (!location.isGeo()) {    // if the location contains geo coordinates, skipping this test hack
            try {
                if (location.getText().startsWith("-")) {
                    return new TestWeather(Integer.parseInt(location.getText()));
                }
                if (location.getText().startsWith("+")) {
                    return new TestWeather(Integer.parseInt(location.getText().substring(1)));
                }
            } catch (NumberFormatException nfe) {
                // ignore and continue
            }
        }

        OpenWeatherMapWeather weather = new OpenWeatherMapWeather(this.context);

        weather.parseCurrentWeather(queryCurrentWeather(location));
        if (weather.isEmpty()) {
            return weather;
        }
        if (weather.getLocation().isGeo()) {    // extracts geo coord from the weather and do onecall with lat/lon
            weather.parseOneCallResult(queryOneCallForecast(weather.getLocation()));
        }
        return weather;
    }

    @Override
    public Weather query(Location location, Locale locale) throws WeatherException {
        return query(location);
        //TODO: what to do with locale?
    }

    JSONObject queryOneCallForecast(Location location) throws WeatherException {
        String url = getOneCallUrl(location, "current,minutely,hourly");
        return queryJSON(url);
    }

    String getOneCallUrl(Location location, String exclude) {
        try {
            return apiBaseUrl + "/onecall?exclude=" + exclude +
                "&appid=" + URLEncoder.encode(key.getKey(), "UTF-8") +
                "&" + location.getQuery();
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    JSONObject queryCurrentWeather(Location location) throws WeatherException {
        String url = getWeatherUrl(location);
        return queryJSON(url);
    }

    String getWeatherUrl(Location location) {
        try {
            return apiBaseUrl + "/weather?appid=" + URLEncoder.encode(key.getKey(), "UTF-8") +
                "&" + location.getQuery();
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    JSONObject queryJSON(String url) throws WeatherException {
        JSONTokener parser = new JSONTokener(readJSON(url));
        try {
            return (JSONObject)parser.nextValue();
        } catch (JSONException e) {
            throw new WeatherException("can't parse JSON: " + e.getLocalizedMessage(), e);
        }
    }

    String readJSON(String url) throws WeatherException {
        StringBuilder result = new StringBuilder();
        Reader reader = getReaderForURL(url);
        char[] buf = new char[1024];
        try {
            int read = reader.read(buf);
            while (read >= 0) {
                result.append(buf, 0 , read);
                read = reader.read(buf);
            }
        } catch (IOException e) {
            throw new WeatherException("can't read weather: " + e.getLocalizedMessage(), e);
        }
        String content = result.toString();
        this.debugDumper.dump(url, content);
        return content;
    }

}
