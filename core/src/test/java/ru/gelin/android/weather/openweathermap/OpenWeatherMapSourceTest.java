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
import androidx.test.core.app.ApplicationProvider;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import ru.gelin.android.weather.Location;
import ru.gelin.android.weather.SimpleLocation;
import ru.gelin.android.weather.TemperatureUnit;
import ru.gelin.android.weather.Weather;
import ru.gelin.android.weather.WeatherException;
import ru.gelin.android.weather.WeatherSource;

import static org.junit.Assert.*;


public class OpenWeatherMapSourceTest {

    private final Context context = ApplicationProvider.getApplicationContext();

    @Test
    public void testQueryOmsk() throws WeatherException {
        WeatherSource source = new OpenWeatherMapSource(context);
        Location location = new SimpleLocation("lat=54.96&lon=73.38", true);
        Weather weather = source.query(location);
        assertNotNull(weather);
        assertFalse(weather.isEmpty());
    }

    @Test
    public void testQueryOmskJSON() throws WeatherException, JSONException {
        OpenWeatherMapSource source = new OpenWeatherMapSource(context);
        Location location = new SimpleLocation("lat=54.96&lon=73.38", true);
        JSONObject json = source.queryCurrentWeather(location);
        assertNotNull(json);
        assertEquals("Omsk", json.getString("name"));
    }

    @Test
    public void testQueryOmskName() throws WeatherException {
        WeatherSource source = new OpenWeatherMapSource(context);
        Location location = new SimpleLocation("q=omsk", false);
        Weather weather = source.query(location);
        assertNotNull(weather);
        assertFalse(weather.isEmpty());
    }

    @Test
    public void testQueryOmskNameJSON() throws WeatherException, JSONException {
        OpenWeatherMapSource source = new OpenWeatherMapSource(context);
        Location location = new SimpleLocation("q=omsk", false);
        JSONObject json = source.queryCurrentWeather(location);
        assertNotNull(json);
        assertEquals("Omsk", json.getString("name"));
    }

    @Test
    public void testQueryOmskForecasts() throws WeatherException {
        WeatherSource source = new OpenWeatherMapSource(context);
        Location location = new SimpleLocation("lat=54.96&lon=73.38&cnt=4", true);
        Weather weather = source.query(location);
        assertNotNull(weather);
        assertFalse(weather.isEmpty());
        assertEquals(4, weather.getConditions().size());
    }

    @Test
    public void testQueryTestLocationPlus() throws WeatherException {
        WeatherSource source = new OpenWeatherMapSource(context);
        Location location = new SimpleLocation("+25", false);
        Weather weather = source.query(location);
        assertNotNull(weather);
        assertFalse(weather.isEmpty());
        assertEquals("Test location", weather.getLocation().getText());
        assertEquals(25, weather.getConditions().get(0).getTemperature(TemperatureUnit.C).getCurrent());
    }

    @Test
    public void testQueryTestLocationMinus() throws WeatherException {
        WeatherSource source = new OpenWeatherMapSource(context);
        Location location = new SimpleLocation("-25", false);
        Weather weather = source.query(location);
        assertNotNull(weather);
        assertFalse(weather.isEmpty());
        assertEquals("Test location", weather.getLocation().getText());
        assertEquals(-25, weather.getConditions().get(0).getTemperature(TemperatureUnit.C).getCurrent());
    }

//    In API 2.5 the city name is not localized
//
//    public void testQueryOmskJSON_RU() throws WeatherException, JSONException {
//        OpenWeatherMapSource source = new OpenWeatherMapSource(getContext());
//        Location location = new SimpleLocation("lat=54.96&lon=73.38&lang=ru", true);
//        JSONObject json = source.queryCurrentWeather(location);
//        assertNotNull(json);
//        assertEquals("Омск", json.getString("name"));
//    }
//
//    public void testQueryOmskNameJSON_RU() throws WeatherException, JSONException {
//        OpenWeatherMapSource source = new OpenWeatherMapSource(getContext());
//        Location location = new SimpleLocation("q=omsk&lang=ru", false);
//        JSONObject json = source.queryCurrentWeather(location);
//        assertNotNull(json);
//        assertEquals("Омск", json.getString("name"));
//    }
//
//    public void testQueryOmskNameJSON_EO() throws WeatherException, JSONException {
//        OpenWeatherMapSource source = new OpenWeatherMapSource(getContext());
//        Location location = new SimpleLocation("q=omsk&lang=eo", false);    //unsupported language
//        JSONObject json = source.queryCurrentWeather(location);
//        assertNotNull(json);
//        assertEquals("Omsk", json.getString("name"));
//    }

}
