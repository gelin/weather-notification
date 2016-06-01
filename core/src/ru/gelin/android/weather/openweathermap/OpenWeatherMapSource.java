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
import ru.gelin.android.weather.*;
import ru.gelin.android.weather.source.DebugDumper;
import ru.gelin.android.weather.source.HttpWeatherSource;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Locale;

/**
 *  Weather source implementation which uses openweathermap.org
 */
public class OpenWeatherMapSource extends HttpWeatherSource implements WeatherSource {

    /** API key */
    static final String API_KEY = "616a1aaacb2a1e3e3ca80c8e78455f76";
    /** Base API URL */
    static final String API_BASE_URL = "http://api.openweathermap.org/data/2.5";
    /** Current weather API URL */
    static final String API_WEATHER_URL = API_BASE_URL + "/weather?APPID=" + API_KEY + "&";
    /** Forecasts API URL */
    static final String API_FORECAST_URL = API_BASE_URL + "/forecast/daily?APPID=" + API_KEY + "&cnt=4&id=";

    private final Context context;
    private final DebugDumper debugDumper;

    public OpenWeatherMapSource(Context context) {
        this.context = context;
        this.debugDumper = new DebugDumper(context, API_BASE_URL);
    }

    @Override
    public Weather query(Location location) throws WeatherException {
        if (location == null) {
            throw new WeatherException("null location");
        }
        if (location.getText().startsWith("-")) {
            return new TestWeather(Integer.parseInt(location.getText()));
        }
        if (location.getText().startsWith("+")) {
            return new TestWeather(Integer.parseInt(location.getText().substring(1)));
        }
        OpenWeatherMapWeather weather = new OpenWeatherMapWeather(this.context);
        weather.parseCurrentWeather(queryCurrentWeather(location));
        if (weather.isEmpty()) {
            return weather;
        }
        weather.parseDailyForecast(queryDailyForecast(weather.getCityId()));
        return weather;
    }

    @Override
    public Weather query(Location location, Locale locale) throws WeatherException {
        return query(location);
        //TODO: what to do with locale?
    }

    JSONObject queryCurrentWeather(Location location) throws WeatherException {
        String url = API_WEATHER_URL + location.getQuery();
        JSONTokener parser = new JSONTokener(readJSON(url));
        try {
            return (JSONObject)parser.nextValue();
        } catch (JSONException e) {
            throw new WeatherException("can't parse weather", e);
        }
    }

    JSONObject queryDailyForecast(int cityId) throws WeatherException {
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
        String content = result.toString();
        this.debugDumper.dump(url, content);
        return content;
    }

}
