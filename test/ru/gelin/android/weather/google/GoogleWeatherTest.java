package ru.gelin.android.weather.google;

import static org.junit.Assert.assertEquals;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Calendar;
import java.util.TimeZone;

import org.junit.Test;

import ru.gelin.android.weather.Temperature;
import ru.gelin.android.weather.UnitSystem;
import ru.gelin.android.weather.Weather;
import ru.gelin.android.weather.WeatherCondition;

public class GoogleWeatherTest {
    
    @Test
    public void testXmlParseEn() throws Exception {
        InputStream xml = getClass().getResourceAsStream("google_weather_api_en.xml");
        Weather weather = new GoogleWeather(new InputStreamReader(xml, "UTF-8"));
        assertEquals("Omsk, Omsk Oblast", weather.getLocation().getText());
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.set(2010, Calendar.DECEMBER, 28, 6, 0, 0);
        assertEquals(calendar.getTime(), weather.getTime());
        assertEquals(UnitSystem.US, weather.getUnitSystem());
        
        assertEquals(4, weather.getConditions().size());
        
        WeatherCondition condition0 = weather.getConditions().get(0);
        assertEquals("Clear", condition0.getConditionText());
        Temperature temp0 = condition0.getTemperature();
        assertEquals(-11, temp0.getCurrent());
        assertEquals(-10, temp0.getLow());
        assertEquals(-4, temp0.getHigh());
        assertEquals("Humidity: 66%", condition0.getHumidityText());
        assertEquals("Wind: SW at 2 mph", condition0.getWindText());
        
        WeatherCondition condition1 = weather.getConditions().get(1);
        assertEquals("Snow Showers", condition1.getConditionText());
        Temperature temp1 = condition1.getTemperature();
        assertEquals(7, temp1.getCurrent());
        assertEquals(-7, temp1.getLow());
        assertEquals(20, temp1.getHigh());
        
        WeatherCondition condition2 = weather.getConditions().get(2);
        assertEquals("Partly Sunny", condition2.getConditionText());
        Temperature temp2 = condition2.getTemperature();
        assertEquals(-10, temp2.getCurrent());
        assertEquals(-14, temp2.getLow());
        assertEquals(-6, temp2.getHigh());
        
        WeatherCondition condition3 = weather.getConditions().get(3);
        assertEquals("Partly Sunny", condition3.getConditionText());
        Temperature temp3 = condition3.getTemperature();
        assertEquals(-22, temp3.getCurrent());
        assertEquals(-29, temp3.getLow());
        assertEquals(-15, temp3.getHigh());
    }
    
    @Test
    public void testTempConvertUS2SI() throws Exception {
        InputStream xml = getClass().getResourceAsStream("google_weather_api_en.xml");
        Weather weather = new GoogleWeather(new InputStreamReader(xml, "UTF-8"));
        
        WeatherCondition condition0 = weather.getConditions().get(0);
        Temperature temp0 = condition0.getTemperature(UnitSystem.SI);
        assertEquals(-24, temp0.getCurrent());
        assertEquals(-23, temp0.getLow());  //(-10 - 32) * 5 / 9
        assertEquals(-20, temp0.getHigh());  //(-4 - 32) * 5 / 9
    }
    
    @Test
    public void testXmlParseRu() throws Exception {
        InputStream xml = getClass().getResourceAsStream("google_weather_api_ru.xml");
        Weather weather = new GoogleWeather(new InputStreamReader(xml, "UTF-8"));
        assertEquals("Omsk, Omsk Oblast", weather.getLocation().getText());
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.set(2010, Calendar.DECEMBER, 28, 6, 0, 0);
        assertEquals(calendar.getTime(), weather.getTime());
        assertEquals(UnitSystem.SI, weather.getUnitSystem());
        
        assertEquals(4, weather.getConditions().size());
        
        WeatherCondition condition0 = weather.getConditions().get(0);
        assertEquals("Ясно", condition0.getConditionText());
        Temperature temp0 = condition0.getTemperature();
        assertEquals(-24, temp0.getCurrent());
        assertEquals(-23, temp0.getLow());
        assertEquals(-20, temp0.getHigh());
        assertEquals("Влажность: 66 %", condition0.getHumidityText());
        assertEquals("Ветер: ЮЗ, 1 м/с", condition0.getWindText());
        
        WeatherCondition condition1 = weather.getConditions().get(1);
        assertEquals("Ливневый снег", condition1.getConditionText());
        Temperature temp1 = condition1.getTemperature();
        assertEquals(-14, temp1.getCurrent());
        assertEquals(-21, temp1.getLow());
        assertEquals(-7, temp1.getHigh());
        
        WeatherCondition condition2 = weather.getConditions().get(2);
        assertEquals("Местами солнечно", condition2.getConditionText());
        Temperature temp2 = condition2.getTemperature();
        assertEquals(-23, temp2.getCurrent());
        assertEquals(-26, temp2.getLow());
        assertEquals(-21, temp2.getHigh());
        
        WeatherCondition condition3 = weather.getConditions().get(3);
        assertEquals("Местами солнечно", condition3.getConditionText());
        Temperature temp3 = condition3.getTemperature();
        assertEquals(-30, temp3.getCurrent());
        assertEquals(-34, temp3.getLow());
        assertEquals(-26, temp3.getHigh());
    }
    
    @Test
    public void testTempConvertSI2US() throws Exception {
        InputStream xml = getClass().getResourceAsStream("google_weather_api_ru.xml");
        Weather weather = new GoogleWeather(new InputStreamReader(xml, "UTF-8"));
        
        WeatherCondition condition0 = weather.getConditions().get(0);
        Temperature temp0 = condition0.getTemperature(UnitSystem.US);
        assertEquals(-11, temp0.getCurrent());
        assertEquals(-9, temp0.getLow());   //-23 * 9 / 5 + 32
        assertEquals(-4, temp0.getHigh());  //-20 * 9 / 5 + 32
    }

}
