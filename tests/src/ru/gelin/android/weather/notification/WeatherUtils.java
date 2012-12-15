package ru.gelin.android.weather.notification;

import android.content.Context;
import android.os.Parcel;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import ru.gelin.android.weather.*;
import ru.gelin.android.weather.google.GoogleWeather;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;
import java.util.TimeZone;

import static android.test.AndroidTestCase.assertEquals;
import static android.test.AndroidTestCase.assertTrue;

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

    public static Weather createOpenWeather(Context context) throws Exception {
        return ru.gelin.android.weather.openweathermap.WeatherUtils.createOpenWeather(context);
    }

    public static Weather createIncompleteOpenWeather(Context context) throws Exception {
        return ru.gelin.android.weather.openweathermap.WeatherUtils.createIncompleteOpenWeather(context);
    }

    public static ru.gelin.android.weather.v_0_2.Weather createWeather_v_0_2() throws Exception {
        return new ru.gelin.android.weather.v_0_2.google.GoogleWeather(
                new InputStreamReader(WeatherUtils.class.getResourceAsStream("google_weather_api_en.xml")));
    }

    public static ru.gelin.android.weather.v_0_2.Weather convert(Weather weather) {
        Parcel parcel = Parcel.obtain();
        ParcelableWeather parcelable = new ParcelableWeather(weather);
        parcelable.writeToParcel(parcel, 0);
        parcel.setDataPosition(0);
        return ru.gelin.android.weather.v_0_2.notification.ParcelableWeather.CREATOR.createFromParcel(parcel);
    }

    public static Weather convert(ru.gelin.android.weather.v_0_2.Weather weather) {
        Parcel parcel = Parcel.obtain();
        ru.gelin.android.weather.v_0_2.notification.ParcelableWeather parcelable =
                new ru.gelin.android.weather.v_0_2.notification.ParcelableWeather(weather);
        parcelable.writeToParcel(parcel, 0);
        parcel.setDataPosition(0);
        return ParcelableWeather.CREATOR.createFromParcel(parcel);
    }
    
    public static void checkWeather(Weather weather, Version version) throws MalformedURLException {
        switch (version) {
        case V_0_2:
            checkWeather_v_0_2(weather);
            break;
        default:
            checkWeather(weather);
        }
    }
    
    public static void checkWeather(Weather weather) throws MalformedURLException {
        assertEquals("Omsk, Omsk Oblast", weather.getLocation().getText());
        
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.set(2010, Calendar.DECEMBER, 28, 6, 0, 0);
        assertEquals(calendar.getTime(), weather.getTime());
        calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        calendar.add(Calendar.MINUTE, -1);
        assertTrue(weather.getQueryTime().after(calendar.getTime()));

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

    public static void checkOpenWeather(Weather weather) throws MalformedURLException {
        assertEquals("Omsk", weather.getLocation().getText());

        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.set(2012, Calendar.SEPTEMBER, 14, 5, 30, 0);
        assertEquals(calendar.getTime(), weather.getTime());
        calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        calendar.add(Calendar.MINUTE, -1);
        assertTrue(weather.getQueryTime().after(calendar.getTime()));

        assertEquals(new URL("http://m.openweathermap.org/city/1496153#forecast"), weather.getForecastURL());

        assertEquals(4, weather.getConditions().size());

        WeatherCondition condition0 = weather.getConditions().get(0);
        assertEquals("Broken clouds", condition0.getConditionText());
        Temperature temp0 = condition0.getTemperature(TemperatureUnit.K);
        assertEquals(283, temp0.getCurrent());
        assertEquals(278, temp0.getLow());
        assertEquals(288, temp0.getHigh());
        Humidity humidity = condition0.getHumidity();
        assertEquals("Humidity: 81%", humidity.getText());
        assertEquals(81, humidity.getValue());
        Wind wind = condition0.getWind(WindSpeedUnit.MPS);
        assertEquals("Wind: SSE, 5 m/s", wind.getText());
        assertEquals(WindDirection.SSE, wind.getDirection());
        assertEquals(5, wind.getSpeed());

        WeatherCondition condition1 = weather.getConditions().get(1);
        assertEquals("Light rain", condition1.getConditionText());
        Temperature temp1 = condition1.getTemperature(TemperatureUnit.K);
        assertEquals(280, temp1.getCurrent());
        assertEquals(278, temp1.getLow());
        assertEquals(281, temp1.getHigh());

        WeatherCondition condition2 = weather.getConditions().get(2);
        assertEquals("Rain", condition2.getConditionText());
        Temperature temp2 = condition2.getTemperature(TemperatureUnit.K);
        assertEquals(282, temp2.getCurrent());
        assertEquals(278, temp2.getLow());
        assertEquals(285, temp2.getHigh());

        WeatherCondition condition3 = weather.getConditions().get(3);
        assertEquals("Light rain", condition3.getConditionText());
        Temperature temp3 = condition3.getTemperature(TemperatureUnit.K);
        assertEquals(282, temp3.getCurrent());
        assertEquals(276, temp3.getLow());
        assertEquals(287, temp3.getHigh());
    }
    
    public static void checkWeather(ru.gelin.android.weather.v_0_2.Weather weather) {
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
    
    @SuppressWarnings("deprecation")
    public static void checkWeather_v_0_2(Weather weather) {
        assertEquals("Omsk, Omsk Oblast", weather.getLocation().getText());
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.set(2010, Calendar.DECEMBER, 28, 6, 0, 0);
        assertEquals(calendar.getTime(), weather.getTime());
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

    public static JSONObject readJSON(String resourceName) throws IOException, JSONException {
        Reader reader = new InputStreamReader(WeatherUtils.class.getResourceAsStream(resourceName));
        StringBuilder buffer = new StringBuilder();
        int c = reader.read();
        while (c >= 0) {
            buffer.append((char)c);
            c = reader.read();
        }
        JSONTokener parser = new JSONTokener(buffer.toString());
        return (JSONObject)parser.nextValue();
    }

}
