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
import android.os.Build;
import androidx.test.core.app.ApplicationProvider;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import ru.gelin.android.weather.Humidity;
import ru.gelin.android.weather.Location;
import ru.gelin.android.weather.PrecipitationPeriod;
import ru.gelin.android.weather.SimpleWeatherCondition;
import ru.gelin.android.weather.Temperature;
import ru.gelin.android.weather.TemperatureUnit;
import ru.gelin.android.weather.WeatherCondition;
import ru.gelin.android.weather.WeatherConditionType;
import ru.gelin.android.weather.WeatherException;
import ru.gelin.android.weather.Wind;
import ru.gelin.android.weather.WindDirection;
import ru.gelin.android.weather.WindSpeedUnit;

import java.io.IOException;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import static org.junit.Assert.*;
import static ru.gelin.android.weather.notification.WeatherUtils.readJSON;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = {Build.VERSION_CODES.P})
public class OpenWeatherMapWeatherTest {

    private final Context context = ApplicationProvider.getApplicationContext();

    @Test(expected = WeatherException.class)
    public void testParseEmptyJSON() throws JSONException, WeatherException {
        JSONTokener parser = new JSONTokener("{}");
        new OpenWeatherMapWeather(context, (JSONObject)parser.nextValue());
    }

    @Test
    public void testParseBadResultCodeJSON() throws JSONException, WeatherException {
        JSONTokener parser = new JSONTokener("{ \"cod\": \"404\"}");
        OpenWeatherMapWeather weather = new OpenWeatherMapWeather(context,
            (JSONObject)parser.nextValue());
        assertNotNull(weather);
        assertTrue(weather.isEmpty());
    }

    @Test
    public void testParseMinimalJSON() throws JSONException, WeatherException {
        JSONTokener parser = new JSONTokener("{\"id\": 1496153,\"cod\": 200}");
        OpenWeatherMapWeather weather = new OpenWeatherMapWeather(context,
            (JSONObject)parser.nextValue());
        assertNotNull(weather);
        assertFalse(weather.isEmpty());
        assertEquals(1496153, weather.getCityId());
        assertEquals("", weather.getLocation().getText());
        WeatherCondition condition = weather.getConditions().get(0);
        assertEquals(Temperature.UNKNOWN, condition.getTemperature(TemperatureUnit.C).getCurrent());
    }

    @Test
    public void testNotNullLocation() {
        OpenWeatherMapWeather weather = new OpenWeatherMapWeather(context);
        assertNotNull(weather.getLocation());
        assertTrue(weather.isEmpty());
    }

    @Test
    public void testNotEmpty() throws Exception {
        OpenWeatherMapWeather weather = WeatherUtils.createIncompleteOpenWeather(context);
        assertNotNull(weather);
        assertFalse(weather.isEmpty());
    }

    @Test
    public void testGetTemperature() throws Exception {
        OpenWeatherMapWeather weather = WeatherUtils.createIncompleteOpenWeather(context);
        WeatherCondition condition = weather.getConditions().get(0);
        assertEquals(16, condition.getTemperature(TemperatureUnit.C).getCurrent());
    }

    @Test
    public void testGetLocation() throws Exception {
        OpenWeatherMapWeather weather = WeatherUtils.createIncompleteOpenWeather(context);
        Location location = weather.getLocation();
        assertNotNull(location);
        assertFalse(location.isEmpty());
        assertEquals("Omsk", location.getText());
        // geo coordinates are taken from the response
        assertTrue(location.isGeo());
        assertEquals("lat=55.0&lon=73.4", location.getQuery());
    }

    @Test
    public void testGetTime() throws Exception {
        OpenWeatherMapWeather weather = WeatherUtils.createIncompleteOpenWeather(context);
        Date time = weather.getTime();
        assertNotNull(time);
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        calendar.set(2020, Calendar.JUNE, 5, 17, 30, 5);
        calendar.set(Calendar.MILLISECOND, 0);
        assertEquals(calendar.getTime(), time);
    }

    @Test
    public void testGetQueryTime() throws Exception {
        long now = System.currentTimeMillis();
        OpenWeatherMapWeather weather = WeatherUtils.createIncompleteOpenWeather(context);
        assertTrue(now <= weather.getQueryTime().getTime());
    }

    @Test
    public void testGetConditionText() throws Exception {
        OpenWeatherMapWeather weather = WeatherUtils.createIncompleteOpenWeather(context);
        WeatherCondition condition = weather.getConditions().get(0);
        String text = condition.getConditionText();
        assertEquals("Scattered clouds", text);
    }

    @Test
    public void testGetLowTemperature() throws Exception {
        OpenWeatherMapWeather weather = WeatherUtils.createIncompleteOpenWeather(context);
        WeatherCondition condition = weather.getConditions().get(0);
        assertEquals(16, condition.getTemperature(TemperatureUnit.C).getLow());
    }

    @Test
    public void testGetHighTemperature() throws Exception {
        OpenWeatherMapWeather weather = WeatherUtils.createIncompleteOpenWeather(context);
        WeatherCondition condition = weather.getConditions().get(0);
        assertEquals(16, condition.getTemperature(TemperatureUnit.C).getHigh());
    }

    @Test
    public void testGetWind() throws Exception {
        OpenWeatherMapWeather weather = WeatherUtils.createIncompleteOpenWeather(context);
        WeatherCondition condition = weather.getConditions().get(0);
        Wind wind = condition.getWind(WindSpeedUnit.MPS);
        assertNotNull(wind);
        assertEquals(3, wind.getSpeed());
        assertEquals(WindDirection.NNW, wind.getDirection());
    }

    @Test
    public void testGetHumidity() throws Exception {
        OpenWeatherMapWeather weather = WeatherUtils.createIncompleteOpenWeather(context);
        WeatherCondition condition = weather.getConditions().get(0);
        Humidity humidity = condition.getHumidity();
        assertNotNull(humidity);
        assertEquals(44, humidity.getValue());
    }

    @Test
    public void testGetTemperatureUnit() throws Exception {
        OpenWeatherMapWeather weather = WeatherUtils.createIncompleteOpenWeather(context);
        WeatherCondition condition = weather.getConditions().get(0);
        assertEquals(TemperatureUnit.K, condition.getTemperature().getTemperatureUnit());
    }

    @Test
    public void testGetCityID() throws Exception {
        OpenWeatherMapWeather weather = WeatherUtils.createIncompleteOpenWeather(context);
        assertEquals(1496153, weather.getCityId());
    }

    @Test
    public void testGetForecastURL() throws Exception {
        OpenWeatherMapWeather weather = WeatherUtils.createIncompleteOpenWeather(context);
        assertEquals(new URL("https://openweathermap.org/city/1496153"), weather.getForecastURL());
    }

    @Test
    public void testForecastsNulls() throws Exception {
        OpenWeatherMapWeather weather = WeatherUtils.createOpenWeather(context);
        assertEquals(8, weather.getConditions().size());
        assertNotNull(weather.getConditions().get(3).getHumidity());
        assertNotNull(weather.getConditions().get(3).getWind());
    }

    @Test
    public void testForecastGetLowTemperature() throws Exception {
        OpenWeatherMapWeather weather = WeatherUtils.createOpenWeather(context);
        List<WeatherCondition> conditions = weather.getConditions();
        assertEquals(8, conditions.size());
        assertEquals(287, conditions.get(0).getTemperature(TemperatureUnit.K).getLow());
        assertEquals(283, conditions.get(1).getTemperature(TemperatureUnit.K).getLow());
        assertEquals(287, conditions.get(2).getTemperature(TemperatureUnit.K).getLow());
        assertEquals(288, conditions.get(3).getTemperature(TemperatureUnit.K).getLow());
        assertEquals(289, conditions.get(4).getTemperature(TemperatureUnit.K).getLow());
        assertEquals(286, conditions.get(5).getTemperature(TemperatureUnit.K).getLow());
        assertEquals(288, conditions.get(6).getTemperature(TemperatureUnit.K).getLow());
        assertEquals(288, conditions.get(7).getTemperature(TemperatureUnit.K).getLow());
    }

    @Test
    public void testForecastGetHighTemperature() throws Exception {
        OpenWeatherMapWeather weather = WeatherUtils.createOpenWeather(context);
        List<WeatherCondition> conditions = weather.getConditions();
        assertEquals(8, conditions.size());
        assertEquals(289, conditions.get(0).getTemperature(TemperatureUnit.K).getHigh());
        assertEquals(295, conditions.get(1).getTemperature(TemperatureUnit.K).getHigh());
        assertEquals(296, conditions.get(2).getTemperature(TemperatureUnit.K).getHigh());
        assertEquals(298, conditions.get(3).getTemperature(TemperatureUnit.K).getHigh());
        assertEquals(299, conditions.get(4).getTemperature(TemperatureUnit.K).getHigh());
        assertEquals(299, conditions.get(5).getTemperature(TemperatureUnit.K).getHigh());
        assertEquals(296, conditions.get(6).getTemperature(TemperatureUnit.K).getHigh());
        assertEquals(300, conditions.get(7).getTemperature(TemperatureUnit.K).getHigh());
    }

    @Test
    public void testForecastGetTemperature() throws Exception {
        OpenWeatherMapWeather weather = WeatherUtils.createOpenWeather(context);
        List<WeatherCondition> conditions = weather.getConditions();
        assertEquals(8, conditions.size());
        //the current temp should come from the city JSON
        assertEquals(289, conditions.get(0).getTemperature(TemperatureUnit.K).getCurrent());
    }

    @Test
    public void testForecastGetPrecipitations() throws Exception {
        OpenWeatherMapWeather weather = WeatherUtils.createOpenWeather(context);
        List<SimpleWeatherCondition> conditions = weather.getOpenWeatherMapConditions();
        assertEquals(8, conditions.size());
        assertEquals(0f, conditions.get(0).getPrecipitation().getValue(PrecipitationPeriod.PERIOD_1H), 0.01f);  //current
        assertEquals(0.67f, conditions.get(1).getPrecipitation().getValue(PrecipitationPeriod.PERIOD_3H), 0.01f);
        assertEquals(0f, conditions.get(2).getPrecipitation().getValue(PrecipitationPeriod.PERIOD_3H), 0.01f);
        assertEquals(0f, conditions.get(3).getPrecipitation().getValue(PrecipitationPeriod.PERIOD_3H), 0.01f);
        assertEquals(1.24f, conditions.get(4).getPrecipitation().getValue(PrecipitationPeriod.PERIOD_3H), 0.01f);
        assertEquals(0.21f, conditions.get(5).getPrecipitation().getValue(PrecipitationPeriod.PERIOD_3H), 0.01f);
        assertEquals(0f, conditions.get(6).getPrecipitation().getValue(PrecipitationPeriod.PERIOD_3H), 0.01f);
        assertEquals(0f, conditions.get(7).getPrecipitation().getValue(PrecipitationPeriod.PERIOD_3H), 0.01f);
    }

    @Test
    public void testForecastGetCloudiness() throws Exception {
        OpenWeatherMapWeather weather = WeatherUtils.createOpenWeather(context);
        List<SimpleWeatherCondition> conditions = weather.getOpenWeatherMapConditions();
        assertEquals(8, conditions.size());
        assertEquals(46, conditions.get(0).getCloudiness().getValue());  //current
        assertEquals(46, conditions.get(1).getCloudiness().getValue());
        assertEquals(25, conditions.get(2).getCloudiness().getValue());
        assertEquals(65, conditions.get(3).getCloudiness().getValue());
        assertEquals(24, conditions.get(4).getCloudiness().getValue());
        assertEquals(3, conditions.get(5).getCloudiness().getValue());
        assertEquals(17, conditions.get(6).getCloudiness().getValue());
        assertEquals(22, conditions.get(7).getCloudiness().getValue());
    }

    @Test
    public void testForecastGetConditionTypes() throws Exception {
        OpenWeatherMapWeather weather = WeatherUtils.createOpenWeather(context);
        List<SimpleWeatherCondition> conditions = weather.getOpenWeatherMapConditions();
        assertEquals(8, conditions.size());
        assertEquals(1, conditions.get(0).getConditionTypes().size());
        assertTrue(conditions.get(0).getConditionTypes().contains(WeatherConditionType.CLOUDS_SCATTERED));
        assertEquals(1, conditions.get(1).getConditionTypes().size());
        assertTrue(conditions.get(1).getConditionTypes().contains(WeatherConditionType.RAIN_LIGHT));
        assertEquals(1, conditions.get(2).getConditionTypes().size());
        assertTrue(conditions.get(2).getConditionTypes().contains(WeatherConditionType.CLOUDS_SCATTERED));
        assertEquals(1, conditions.get(3).getConditionTypes().size());
        assertTrue(conditions.get(3).getConditionTypes().contains(WeatherConditionType.CLOUDS_BROKEN));
        assertEquals(1, conditions.get(4).getConditionTypes().size());
        assertTrue(conditions.get(4).getConditionTypes().contains(WeatherConditionType.RAIN_LIGHT));
        assertEquals(1, conditions.get(5).getConditionTypes().size());
        assertTrue(conditions.get(5).getConditionTypes().contains(WeatherConditionType.RAIN_LIGHT));
        assertEquals(1, conditions.get(6).getConditionTypes().size());
        assertTrue(conditions.get(6).getConditionTypes().contains(WeatherConditionType.CLOUDS_FEW));
        assertEquals(1, conditions.get(7).getConditionTypes().size());
        assertTrue(conditions.get(7).getConditionTypes().contains(WeatherConditionType.CLOUDS_FEW));
    }

    @Test
    public void testForecastGetConditionText() throws Exception {
        OpenWeatherMapWeather weather = WeatherUtils.createOpenWeather(context);
        List<WeatherCondition> conditions = weather.getConditions();
        assertEquals(8, conditions.size());
        assertEquals("Scattered clouds", conditions.get(0).getConditionText());
        assertEquals("Light rain", conditions.get(1).getConditionText());
        assertEquals("Scattered clouds", conditions.get(2).getConditionText());
        assertEquals("Broken clouds", conditions.get(3).getConditionText());
        assertEquals("Light rain", conditions.get(4).getConditionText());
        assertEquals("Light rain", conditions.get(5).getConditionText());
        assertEquals("Few clouds", conditions.get(6).getConditionText());
        assertEquals("Few clouds", conditions.get(7).getConditionText());
    }

    @Test
    public void testForecastGetWind() throws Exception {
        OpenWeatherMapWeather weather = WeatherUtils.createOpenWeather(context);
        List<WeatherCondition> conditions = weather.getConditions();
        assertEquals(8, conditions.size());
        assertEquals(3, conditions.get(0).getWind().getSpeed());
        assertEquals(WindDirection.NNW, conditions.get(0).getWind().getDirection());
        assertEquals(6, conditions.get(1).getWind().getSpeed());
        assertEquals(WindDirection.N, conditions.get(1).getWind().getDirection());
        assertEquals(4, conditions.get(2).getWind().getSpeed());
        assertEquals(WindDirection.NNW, conditions.get(2).getWind().getDirection());
        assertEquals(4, conditions.get(3).getWind().getSpeed());
        assertEquals(WindDirection.WNW, conditions.get(3).getWind().getDirection());
        assertEquals(5, conditions.get(4).getWind().getSpeed());
        assertEquals(WindDirection.NNW, conditions.get(4).getWind().getDirection());
        assertEquals(3, conditions.get(5).getWind().getSpeed());
        assertEquals(WindDirection.N, conditions.get(5).getWind().getDirection());
        assertEquals(6, conditions.get(6).getWind().getSpeed());
        assertEquals(WindDirection.N, conditions.get(6).getWind().getDirection());
        assertEquals(4, conditions.get(7).getWind().getSpeed());
        assertEquals(WindDirection.WSW, conditions.get(7).getWind().getDirection());
    }

    @Test
    public void testForecastGetHumidity() throws Exception {
        OpenWeatherMapWeather weather = WeatherUtils.createOpenWeather(context);
        List<WeatherCondition> conditions = weather.getConditions();
        assertEquals(8, conditions.size());
        assertEquals(44, conditions.get(0).getHumidity().getValue());
        assertEquals(46, conditions.get(1).getHumidity().getValue());
        assertEquals(38, conditions.get(2).getHumidity().getValue());
        assertEquals(42, conditions.get(3).getHumidity().getValue());
        assertEquals(51, conditions.get(4).getHumidity().getValue());
        assertEquals(41, conditions.get(5).getHumidity().getValue());
        assertEquals(42, conditions.get(6).getHumidity().getValue());
        assertEquals(48, conditions.get(7).getHumidity().getValue());
    }

    @Test
    public void testParseNoHumidity() throws IOException, JSONException, WeatherException {
        OpenWeatherMapWeather weather = new OpenWeatherMapWeather(context,
            readJSON("omsk_name_no_humidity_2.5.json"));
        assertNotNull(weather);
        assertFalse(weather.isEmpty());
        WeatherCondition condition = weather.getConditions().get(0);
        assertEquals(21, condition.getTemperature(TemperatureUnit.C).getCurrent());
    }

}
