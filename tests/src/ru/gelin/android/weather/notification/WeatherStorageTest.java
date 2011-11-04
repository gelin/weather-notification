/*
 *  Android Weather Notification.
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

package ru.gelin.android.weather.notification;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Calendar;
import java.util.TimeZone;

import ru.gelin.android.weather.Humidity;
import ru.gelin.android.weather.Temperature;
import ru.gelin.android.weather.TemperatureUnit;
import ru.gelin.android.weather.Weather;
import ru.gelin.android.weather.WeatherCondition;
import ru.gelin.android.weather.Wind;
import ru.gelin.android.weather.WindDirection;
import ru.gelin.android.weather.WindSpeedUnit;
import ru.gelin.android.weather.google.GoogleWeather;
import android.test.AndroidTestCase;

public class WeatherStorageTest extends AndroidTestCase {

    public void testSaveLoad() throws Exception {
        InputStream xml1 = getClass().getResourceAsStream("google_weather_api_en.xml");
        InputStream xml2 = getClass().getResourceAsStream("google_weather_api_en.xml");
        GoogleWeather weatherToSave = GoogleWeather.parse(
                new InputStreamReader(xml1, "UTF-8"), new InputStreamReader(xml2, "UTF-8"));
        WeatherStorage storage = new WeatherStorage(getContext());
        storage.save(weatherToSave);
        Weather weather = storage.load();
        
        assertEquals("Omsk, Omsk Oblast", weather.getLocation().getText());
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.set(2010, Calendar.DECEMBER, 28, 6, 0, 0);
        assertEquals(calendar.getTime(), weather.getTime());
        assertEquals(4, weather.getConditions().size());
        
        WeatherCondition condition0 = weather.getConditions().get(0);
        assertEquals("Clear", condition0.getConditionText());
        Temperature temp0 = condition0.getTemperature(TemperatureUnit.F);
        assertEquals(-11, temp0.getCurrent());
        assertEquals(-10, temp0.getLow());
        assertEquals(-4, temp0.getHigh());
        Humidity humidity = condition0.getHumidity();
        assertEquals("Humidity: 66%", humidity.getText());
        assertEquals(66, humidity.getValue());
        Wind wind = condition0.getWind(WindSpeedUnit.MPH);
        assertEquals("Wind: SW at 2 mph", wind.getText());
        assertEquals(WindDirection.SW, wind.getDirection());
        assertEquals(2, wind.getSpeed());
        
        WeatherCondition condition1 = weather.getConditions().get(1);
        assertEquals("Snow Showers", condition1.getConditionText());
        Temperature temp1 = condition1.getTemperature(TemperatureUnit.F);
        assertEquals(7, temp1.getCurrent());
        assertEquals(-7, temp1.getLow());
        assertEquals(20, temp1.getHigh());
        
        WeatherCondition condition2 = weather.getConditions().get(2);
        assertEquals("Partly Sunny", condition2.getConditionText());
        Temperature temp2 = condition2.getTemperature(TemperatureUnit.F);
        assertEquals(-10, temp2.getCurrent());
        assertEquals(-14, temp2.getLow());
        assertEquals(-6, temp2.getHigh());
        
        WeatherCondition condition3 = weather.getConditions().get(3);
        assertEquals("Partly Sunny", condition3.getConditionText());
        Temperature temp3 = condition3.getTemperature(TemperatureUnit.F);
        assertEquals(-22, temp3.getCurrent());
        assertEquals(-29, temp3.getLow());
        assertEquals(-15, temp3.getHigh());
    }
    
    public void testBackwardCompatibility() throws Exception {
        InputStream xml1 = getClass().getResourceAsStream("google_weather_api_en.xml");
        InputStream xml2 = getClass().getResourceAsStream("google_weather_api_en.xml");
        GoogleWeather newWeather = GoogleWeather.parse(
                new InputStreamReader(xml1, "UTF-8"), new InputStreamReader(xml2, "UTF-8"));
        WeatherStorage newStorage = new WeatherStorage(getContext());
        newStorage.save(newWeather);
        
        ru.gelin.android.weather.v_0_2.notification.WeatherStorage storage = 
            new ru.gelin.android.weather.v_0_2.notification.WeatherStorage(getContext());
        ru.gelin.android.weather.v_0_2.Weather weather = storage.load();
        
        assertEquals("Omsk, Omsk Oblast", weather.getLocation().getText());
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.set(2010, Calendar.DECEMBER, 28, 6, 0, 0);
        assertEquals(calendar.getTime(), weather.getTime());
        assertEquals(ru.gelin.android.weather.v_0_2.UnitSystem.US, weather.getUnitSystem());
        assertEquals(4, weather.getConditions().size());
        
        ru.gelin.android.weather.v_0_2.WeatherCondition condition0 = weather.getConditions().get(0);
        assertEquals("Clear", condition0.getConditionText());
        ru.gelin.android.weather.v_0_2.Temperature temp0 = 
            condition0.getTemperature(ru.gelin.android.weather.v_0_2.UnitSystem.US);
        assertEquals(-11, temp0.getCurrent());
        assertEquals(-10, temp0.getLow());
        assertEquals(-4, temp0.getHigh());
        assertEquals("Humidity: 66%", condition0.getHumidityText());
        assertEquals("Wind: SW at 2 mph", condition0.getWindText());
        
        ru.gelin.android.weather.v_0_2.WeatherCondition condition1 = weather.getConditions().get(1);
        assertEquals("Snow Showers", condition1.getConditionText());
        ru.gelin.android.weather.v_0_2.Temperature temp1 = 
            condition1.getTemperature(ru.gelin.android.weather.v_0_2.UnitSystem.US);
        assertEquals(7, temp1.getCurrent());
        assertEquals(-7, temp1.getLow());
        assertEquals(20, temp1.getHigh());
        
        ru.gelin.android.weather.v_0_2.WeatherCondition condition2 = weather.getConditions().get(2);
        assertEquals("Partly Sunny", condition2.getConditionText());
        ru.gelin.android.weather.v_0_2.Temperature temp2 = 
            condition2.getTemperature(ru.gelin.android.weather.v_0_2.UnitSystem.US);
        assertEquals(-10, temp2.getCurrent());
        assertEquals(-14, temp2.getLow());
        assertEquals(-6, temp2.getHigh());
        
        ru.gelin.android.weather.v_0_2.WeatherCondition condition3 = weather.getConditions().get(3);
        assertEquals("Partly Sunny", condition3.getConditionText());
        ru.gelin.android.weather.v_0_2.Temperature temp3 = 
            condition3.getTemperature(ru.gelin.android.weather.v_0_2.UnitSystem.US);
        assertEquals(-22, temp3.getCurrent());
        assertEquals(-29, temp3.getLow());
        assertEquals(-15, temp3.getHigh());
    }

}
