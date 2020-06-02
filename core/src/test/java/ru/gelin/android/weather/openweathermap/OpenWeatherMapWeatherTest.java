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

    @Test
    public void testNotEmpty() throws Exception {
        OpenWeatherMapWeather weather = WeatherUtils.createIncompleteOpenWeather(context);
        assertNotNull(weather);
        assertFalse(weather.isEmpty());
    }

    @Test
    public void testNotNullLocation() {
        OpenWeatherMapWeather weather = new OpenWeatherMapWeather(context);
        assertNotNull(weather.getLocation());
        assertTrue(weather.isEmpty());
    }

    @Test
    public void testGetTemperature() throws Exception {
        OpenWeatherMapWeather weather = WeatherUtils.createIncompleteOpenWeather(context);
        WeatherCondition condition = weather.getConditions().get(0);
        assertEquals(21, condition.getTemperature(TemperatureUnit.C).getCurrent());
    }

    @Test
    public void testParseEmptyJSON() throws JSONException {
        JSONTokener parser = new JSONTokener("{}");
        try {
            new OpenWeatherMapWeather(context, (JSONObject)parser.nextValue());
            fail();
        } catch (WeatherException e) {
            //passed
        }
    }

    @Test
    public void testGetLocation() throws Exception {
        OpenWeatherMapWeather weather = WeatherUtils.createIncompleteOpenWeather(context);
        Location location = weather.getLocation();
        assertNotNull(location);
        assertEquals("Omsk", location.getText());
    }

    @Test
    public void testGetTime() throws Exception {
        OpenWeatherMapWeather weather = WeatherUtils.createIncompleteOpenWeather(context);
        Date time = weather.getTime();
        assertNotNull(time);
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
        calendar.set(2013, Calendar.AUGUST, 15, 14, 0, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        assertEquals(calendar.getTime(), time);
    }

    @Test
    public void testGetQueryTime() throws Exception {
        long now = System.currentTimeMillis();
        OpenWeatherMapWeather weather = WeatherUtils.createIncompleteOpenWeather(context);
        assertTrue(now < weather.getQueryTime().getTime());
    }

    @Test
    public void testGetConditionText() throws Exception {
        OpenWeatherMapWeather weather = WeatherUtils.createIncompleteOpenWeather(context);
        WeatherCondition condition = weather.getConditions().get(0);
        String text = condition.getConditionText();
        assertEquals("Sky is clear", text);
    }

    @Test
    public void testGetLowTemperature() throws Exception {
        OpenWeatherMapWeather weather = WeatherUtils.createIncompleteOpenWeather(context);
        WeatherCondition condition = weather.getConditions().get(0);
        assertEquals(21, condition.getTemperature(TemperatureUnit.C).getLow());
    }

    @Test
    public void testGetHighTemperature() throws Exception {
        OpenWeatherMapWeather weather = WeatherUtils.createIncompleteOpenWeather(context);
        WeatherCondition condition = weather.getConditions().get(0);
        assertEquals(21, condition.getTemperature(TemperatureUnit.C).getHigh());
    }

    @Test
    public void testGetWind() throws Exception {
        OpenWeatherMapWeather weather = WeatherUtils.createIncompleteOpenWeather(context);
        WeatherCondition condition = weather.getConditions().get(0);
        Wind wind = condition.getWind(WindSpeedUnit.MPS);
        assertNotNull(wind);
        assertEquals(4, wind.getSpeed());
        assertEquals(WindDirection.N, wind.getDirection());
    }

    @Test
    public void testGetHumidity() throws Exception {
        OpenWeatherMapWeather weather = WeatherUtils.createIncompleteOpenWeather(context);
        WeatherCondition condition = weather.getConditions().get(0);
        Humidity humidity = condition.getHumidity();
        assertNotNull(humidity);
        assertEquals(56, humidity.getValue());
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
        assertEquals(new URL("http://openweathermap.org/city/1496153"), weather.getForecastURL());
    }

    @Test
    public void testForecastsNulls() throws Exception {
        OpenWeatherMapWeather weather = WeatherUtils.createOpenWeather(context);
        assertEquals(4, weather.getConditions().size());
        assertNotNull(weather.getConditions().get(3).getHumidity());
        assertNotNull(weather.getConditions().get(3).getWind());
    }

    @Test
    public void testForecastGetLowTemperature() throws Exception {
        OpenWeatherMapWeather weather = WeatherUtils.createOpenWeather(context);
        List<WeatherCondition> conditions = weather.getConditions();
        assertEquals(4, conditions.size());
        assertEquals(287, conditions.get(0).getTemperature(TemperatureUnit.K).getLow());
        assertEquals(284, conditions.get(1).getTemperature(TemperatureUnit.K).getLow());
        assertEquals(282, conditions.get(2).getTemperature(TemperatureUnit.K).getLow());
        assertEquals(283, conditions.get(3).getTemperature(TemperatureUnit.K).getLow());
    }

    @Test
    public void testForecastGetHighTemperature() throws Exception {
        OpenWeatherMapWeather weather = WeatherUtils.createOpenWeather(context);
        List<WeatherCondition> conditions = weather.getConditions();
        assertEquals(4, conditions.size());
        assertEquals(294, conditions.get(0).getTemperature(TemperatureUnit.K).getHigh());
        assertEquals(293, conditions.get(1).getTemperature(TemperatureUnit.K).getHigh());
        assertEquals(293, conditions.get(2).getTemperature(TemperatureUnit.K).getHigh());
        assertEquals(295, conditions.get(3).getTemperature(TemperatureUnit.K).getHigh());
    }

    @Test
    public void testForecastGetTemperature() throws Exception {
        OpenWeatherMapWeather weather = WeatherUtils.createOpenWeather(context);
        List<WeatherCondition> conditions = weather.getConditions();
        assertEquals(4, conditions.size());
        //the current temp should come from the city JSON
        assertEquals(294, conditions.get(0).getTemperature(TemperatureUnit.K).getCurrent());
    }

    @Test
    public void testForecastGetPrecipitations() throws Exception {
        OpenWeatherMapWeather weather = WeatherUtils.createOpenWeather(context);
        List<SimpleWeatherCondition> conditions = weather.getOpenWeatherMapConditions();
        assertEquals(4, conditions.size());
        assertEquals(0f, conditions.get(0).getPrecipitation().getValue(PrecipitationPeriod.PERIOD_1H), 0.01f);  //current
        assertEquals(1f, conditions.get(1).getPrecipitation().getValue(PrecipitationPeriod.PERIOD_1H), 0.01f);
        assertEquals(2f, conditions.get(2).getPrecipitation().getValue(PrecipitationPeriod.PERIOD_1H), 0.01f);
        assertEquals(3f, conditions.get(3).getPrecipitation().getValue(PrecipitationPeriod.PERIOD_1H), 0.01f);
    }

    @Test
    public void testForecastGetCloudiness() throws Exception {
        OpenWeatherMapWeather weather = WeatherUtils.createOpenWeather(context);
        List<SimpleWeatherCondition> conditions = weather.getOpenWeatherMapConditions();
        assertEquals(4, conditions.size());
        assertEquals(0, conditions.get(0).getCloudiness().getValue());  //current
        assertEquals(18, conditions.get(1).getCloudiness().getValue());
        assertEquals(0, conditions.get(2).getCloudiness().getValue());
        assertEquals(22, conditions.get(3).getCloudiness().getValue());
    }

    @Test
    public void testForecastGetConditionTypes() throws Exception {
        OpenWeatherMapWeather weather = WeatherUtils.createOpenWeather(context);
        List<SimpleWeatherCondition> conditions = weather.getOpenWeatherMapConditions();
        assertEquals(4, conditions.size());
        assertEquals(1, conditions.get(0).getConditionTypes().size());
        assertTrue(conditions.get(0).getConditionTypes().contains(WeatherConditionType.CLOUDS_CLEAR));
        assertEquals(2, conditions.get(1).getConditionTypes().size());
        assertTrue(conditions.get(1).getConditionTypes().contains(WeatherConditionType.CLOUDS_FEW));
        assertTrue(conditions.get(1).getConditionTypes().contains(WeatherConditionType.RAIN_LIGHT));
        assertEquals(2, conditions.get(2).getConditionTypes().size());
        assertTrue(conditions.get(2).getConditionTypes().contains(WeatherConditionType.CLOUDS_BROKEN));
        assertTrue(conditions.get(2).getConditionTypes().contains(WeatherConditionType.RAIN));
        assertEquals(2, conditions.get(3).getConditionTypes().size());
        assertTrue(conditions.get(3).getConditionTypes().contains(WeatherConditionType.CLOUDS_OVERCAST));
        assertTrue(conditions.get(3).getConditionTypes().contains(WeatherConditionType.RAIN_SHOWER));
    }

    @Test
    public void testForecastGetConditionText() throws Exception {
        OpenWeatherMapWeather weather = WeatherUtils.createOpenWeather(context);
        List<WeatherCondition> conditions = weather.getConditions();
        assertEquals(4, conditions.size());
        assertEquals("Sky is clear", conditions.get(0).getConditionText());
        assertEquals("Light rain", conditions.get(1).getConditionText());
        assertEquals("Rain", conditions.get(2).getConditionText());
        assertEquals("Shower rain", conditions.get(3).getConditionText());
    }

    @Test
    public void testParseNoHumidity() throws IOException, JSONException, WeatherException {
        OpenWeatherMapWeather weather = new OpenWeatherMapWeather(context,
                readJSON(context, "omsk_name_no_humidity_2.5.json"));
        assertNotNull(weather);
        assertFalse(weather.isEmpty());
        WeatherCondition condition = weather.getConditions().get(0);
        assertEquals(21, condition.getTemperature(TemperatureUnit.C).getCurrent());
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
    public void testParseBadResultCodeJSON() throws JSONException, WeatherException {
        JSONTokener parser = new JSONTokener("{ \"cod\": \"404\"}");
        OpenWeatherMapWeather weather = new OpenWeatherMapWeather(context,
                (JSONObject)parser.nextValue());
        assertNotNull(weather);
        assertTrue(weather.isEmpty());
    }

    @Test
    public void testForecastGetWind() throws Exception {
        OpenWeatherMapWeather weather = WeatherUtils.createOpenWeather(context);
        List<WeatherCondition> conditions = weather.getConditions();
        assertEquals(4, conditions.size());
        assertEquals(4, conditions.get(0).getWind().getSpeed());
        assertEquals(WindDirection.N, conditions.get(0).getWind().getDirection());
        assertEquals(2, conditions.get(1).getWind().getSpeed());
        assertEquals(WindDirection.N, conditions.get(1).getWind().getDirection());
        assertEquals(2, conditions.get(2).getWind().getSpeed());
        assertEquals(WindDirection.SW, conditions.get(2).getWind().getDirection());
        assertEquals(2, conditions.get(3).getWind().getSpeed());
        assertEquals(WindDirection.SW, conditions.get(3).getWind().getDirection());
    }

    @Test
    public void testForecastGetHumidity() throws Exception {
        OpenWeatherMapWeather weather = WeatherUtils.createOpenWeather(context);
        List<WeatherCondition> conditions = weather.getConditions();
        assertEquals(4, conditions.size());
        assertEquals(56, conditions.get(0).getHumidity().getValue());
        assertEquals(98, conditions.get(1).getHumidity().getValue());
        assertEquals(93, conditions.get(2).getHumidity().getValue());
        assertEquals(90, conditions.get(3).getHumidity().getValue());
    }

}
