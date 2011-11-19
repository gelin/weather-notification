package ru.gelin.android.weather.notification;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Calendar;
import java.util.TimeZone;

import android.test.AndroidTestCase;

import ru.gelin.android.weather.Humidity;
import ru.gelin.android.weather.Temperature;
import ru.gelin.android.weather.TemperatureUnit;
import ru.gelin.android.weather.Weather;
import ru.gelin.android.weather.WeatherCondition;
import ru.gelin.android.weather.Wind;
import ru.gelin.android.weather.WindDirection;
import ru.gelin.android.weather.WindSpeedUnit;
import ru.gelin.android.weather.google.GoogleWeather;

public class WeatherUtils {
    
    public static enum Version {
        V_0_2, V_0_3
    }
    
    private WeatherUtils() {
        //avoid instantiation
    }
    
    public static Weather createWeather() throws Exception {
        InputStream xml1 = WeatherUtils.class.getResourceAsStream("google_weather_api_en.xml");
        InputStream xml2 = WeatherUtils.class.getResourceAsStream("google_weather_api_en.xml");
        GoogleWeather weather = GoogleWeather.parse(
                new InputStreamReader(xml1, "UTF-8"), new InputStreamReader(xml2, "UTF-8"));
        return weather;
    }
    
    public static void checkWeather(Weather weather, Version version) {
        switch(version) {
        case V_0_2:
            checkWeather_v_0_2(weather);
            break;
        default:
            checkWeather(weather);
        }
    }
    
    public static void checkWeather(Weather weather) {
        AndroidTestCase.assertEquals("Omsk, Omsk Oblast", weather.getLocation().getText());
        
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.set(2010, Calendar.DECEMBER, 28, 6, 0, 0);
        AndroidTestCase.assertEquals(calendar.getTime(), weather.getTime());
        calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        calendar.add(Calendar.MINUTE, -1);
        AndroidTestCase.assertTrue(weather.getQueryTime().after(calendar.getTime()));
        
        AndroidTestCase.assertEquals(4, weather.getConditions().size());
        
        WeatherCondition condition0 = weather.getConditions().get(0);
        AndroidTestCase.assertEquals("Clear", condition0.getConditionText());
        Temperature temp0 = condition0.getTemperature(TemperatureUnit.F);
        AndroidTestCase.assertEquals(-11, temp0.getCurrent());
        AndroidTestCase.assertEquals(-10, temp0.getLow());
        AndroidTestCase.assertEquals(-4, temp0.getHigh());
        Humidity humidity = condition0.getHumidity();
        AndroidTestCase.assertEquals("Humidity: 66%", humidity.getText());
        AndroidTestCase.assertEquals(66, humidity.getValue());
        Wind wind = condition0.getWind(WindSpeedUnit.MPH);
        AndroidTestCase.assertEquals("Wind: SW at 2 mph", wind.getText());
        AndroidTestCase.assertEquals(WindDirection.SW, wind.getDirection());
        AndroidTestCase.assertEquals(2, wind.getSpeed());
        
        WeatherCondition condition1 = weather.getConditions().get(1);
        AndroidTestCase.assertEquals("Snow Showers", condition1.getConditionText());
        Temperature temp1 = condition1.getTemperature(TemperatureUnit.F);
        AndroidTestCase.assertEquals(7, temp1.getCurrent());
        AndroidTestCase.assertEquals(-7, temp1.getLow());
        AndroidTestCase.assertEquals(20, temp1.getHigh());
        
        WeatherCondition condition2 = weather.getConditions().get(2);
        AndroidTestCase.assertEquals("Partly Sunny", condition2.getConditionText());
        Temperature temp2 = condition2.getTemperature(TemperatureUnit.F);
        AndroidTestCase.assertEquals(-10, temp2.getCurrent());
        AndroidTestCase.assertEquals(-14, temp2.getLow());
        AndroidTestCase.assertEquals(-6, temp2.getHigh());
        
        WeatherCondition condition3 = weather.getConditions().get(3);
        AndroidTestCase.assertEquals("Partly Sunny", condition3.getConditionText());
        Temperature temp3 = condition3.getTemperature(TemperatureUnit.F);
        AndroidTestCase.assertEquals(-22, temp3.getCurrent());
        AndroidTestCase.assertEquals(-29, temp3.getLow());
        AndroidTestCase.assertEquals(-15, temp3.getHigh());
    }
    
    public static void checkWeather(ru.gelin.android.weather.v_0_2.Weather weather) {
        AndroidTestCase.assertEquals("Omsk, Omsk Oblast", weather.getLocation().getText());
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.set(2010, Calendar.DECEMBER, 28, 6, 0, 0);
        AndroidTestCase.assertEquals(calendar.getTime(), weather.getTime());
        AndroidTestCase.assertEquals(ru.gelin.android.weather.v_0_2.UnitSystem.US, weather.getUnitSystem());
        AndroidTestCase.assertEquals(4, weather.getConditions().size());
        
        ru.gelin.android.weather.v_0_2.WeatherCondition condition0 = weather.getConditions().get(0);
        AndroidTestCase.assertEquals("Clear", condition0.getConditionText());
        ru.gelin.android.weather.v_0_2.Temperature temp0 = 
            condition0.getTemperature(ru.gelin.android.weather.v_0_2.UnitSystem.US);
        AndroidTestCase.assertEquals(-11, temp0.getCurrent());
        AndroidTestCase.assertEquals(-10, temp0.getLow());
        AndroidTestCase.assertEquals(-4, temp0.getHigh());
        AndroidTestCase.assertEquals("Humidity: 66%", condition0.getHumidityText());
        AndroidTestCase.assertEquals("Wind: SW at 2 mph", condition0.getWindText());
        
        ru.gelin.android.weather.v_0_2.WeatherCondition condition1 = weather.getConditions().get(1);
        AndroidTestCase.assertEquals("Snow Showers", condition1.getConditionText());
        ru.gelin.android.weather.v_0_2.Temperature temp1 = 
            condition1.getTemperature(ru.gelin.android.weather.v_0_2.UnitSystem.US);
        AndroidTestCase.assertEquals(7, temp1.getCurrent());
        AndroidTestCase.assertEquals(-7, temp1.getLow());
        AndroidTestCase.assertEquals(20, temp1.getHigh());
        
        ru.gelin.android.weather.v_0_2.WeatherCondition condition2 = weather.getConditions().get(2);
        AndroidTestCase.assertEquals("Partly Sunny", condition2.getConditionText());
        ru.gelin.android.weather.v_0_2.Temperature temp2 = 
            condition2.getTemperature(ru.gelin.android.weather.v_0_2.UnitSystem.US);
        AndroidTestCase.assertEquals(-10, temp2.getCurrent());
        AndroidTestCase.assertEquals(-14, temp2.getLow());
        AndroidTestCase.assertEquals(-6, temp2.getHigh());
        
        ru.gelin.android.weather.v_0_2.WeatherCondition condition3 = weather.getConditions().get(3);
        AndroidTestCase.assertEquals("Partly Sunny", condition3.getConditionText());
        ru.gelin.android.weather.v_0_2.Temperature temp3 = 
            condition3.getTemperature(ru.gelin.android.weather.v_0_2.UnitSystem.US);
        AndroidTestCase.assertEquals(-22, temp3.getCurrent());
        AndroidTestCase.assertEquals(-29, temp3.getLow());
        AndroidTestCase.assertEquals(-15, temp3.getHigh());
    }
    
    @SuppressWarnings("deprecation")
    public static void checkWeather_v_0_2(Weather weather) {
        AndroidTestCase.assertEquals("Omsk, Omsk Oblast", weather.getLocation().getText());
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.set(2010, Calendar.DECEMBER, 28, 6, 0, 0);
        AndroidTestCase.assertEquals(calendar.getTime(), weather.getTime());
        AndroidTestCase.assertEquals(4, weather.getConditions().size());
        
        WeatherCondition condition0 = weather.getConditions().get(0);
        AndroidTestCase.assertEquals("Clear", condition0.getConditionText());
        Temperature temp0 = condition0.getTemperature();
        AndroidTestCase.assertEquals(-11, temp0.getCurrent());
        AndroidTestCase.assertEquals(-10, temp0.getLow());
        AndroidTestCase.assertEquals(-4, temp0.getHigh());
        AndroidTestCase.assertEquals("Humidity: 66%", condition0.getHumidityText());
        AndroidTestCase.assertEquals("Wind: SW at 2 mph", condition0.getWindText());
        
        WeatherCondition condition1 = weather.getConditions().get(1);
        AndroidTestCase.assertEquals("Snow Showers", condition1.getConditionText());
        Temperature temp1 = condition1.getTemperature();
        AndroidTestCase.assertEquals(7, temp1.getCurrent());
        AndroidTestCase.assertEquals(-7, temp1.getLow());
        AndroidTestCase.assertEquals(20, temp1.getHigh());
        
        WeatherCondition condition2 = weather.getConditions().get(2);
        AndroidTestCase.assertEquals("Partly Sunny", condition2.getConditionText());
        Temperature temp2 = condition2.getTemperature();
        AndroidTestCase.assertEquals(-10, temp2.getCurrent());
        AndroidTestCase.assertEquals(-14, temp2.getLow());
        AndroidTestCase.assertEquals(-6, temp2.getHigh());
        
        WeatherCondition condition3 = weather.getConditions().get(3);
        AndroidTestCase.assertEquals("Partly Sunny", condition3.getConditionText());
        Temperature temp3 = condition3.getTemperature();
        AndroidTestCase.assertEquals(-22, temp3.getCurrent());
        AndroidTestCase.assertEquals(-29, temp3.getLow());
        AndroidTestCase.assertEquals(-15, temp3.getHigh());
    }

}
