package ru.gelin.android.weather.openweathermap;

import android.test.AndroidTestCase;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import ru.gelin.android.weather.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class OpenWeatherMapWeatherTest extends AndroidTestCase {

    public void testNotEmpty() throws WeatherException, IOException, JSONException {
        OpenWeatherMapWeather weather = new OpenWeatherMapWeather(readJSON("omsk_city.json"));
        assertNotNull(weather);
        assertFalse(weather.isEmpty());
    }

    public void testGetTemperature() throws IOException, JSONException, WeatherException {
        OpenWeatherMapWeather weather = new OpenWeatherMapWeather(readJSON("omsk_city.json"));
        WeatherCondition condition = weather.getConditions().get(0);
        assertEquals(15, condition.getTemperature(TemperatureUnit.C).getCurrent());
    }

    public void testParseEmptyJSON() throws JSONException {
        JSONTokener parser = new JSONTokener("{}");
        try {
            new OpenWeatherMapWeather((JSONObject)parser.nextValue());
            fail();
        } catch (WeatherException e) {
            //passed
        }
    }

    public void testGetLocation() throws IOException, JSONException, WeatherException {
        OpenWeatherMapWeather weather = new OpenWeatherMapWeather(readJSON("omsk_city.json"));
        Location location = weather.getLocation();
        assertNotNull(location);
        assertEquals("Omsk", location.getText());
    }

    public void testGetTime() throws IOException, JSONException, WeatherException {
        OpenWeatherMapWeather weather = new OpenWeatherMapWeather(readJSON("omsk_city.json"));
        Date time = weather.getTime();
        assertNotNull(time);
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
        calendar.set(2012, Calendar.AUGUST, 28, 16, 00, 00);
        calendar.set(Calendar.MILLISECOND, 0);
        assertEquals(calendar.getTime(), time);
    }

    public void testGetQueryTime() throws IOException, JSONException, WeatherException {
        long now = System.currentTimeMillis();
        OpenWeatherMapWeather weather = new OpenWeatherMapWeather(readJSON("omsk_city.json"));
        assertTrue(now < weather.getQueryTime().getTime());
    }

    public void testGetConditionText() throws IOException, JSONException, WeatherException {
        OpenWeatherMapWeather weather = new OpenWeatherMapWeather(readJSON("omsk_city.json"));
        WeatherCondition condition = weather.getConditions().get(0);
        String text = condition.getConditionText();
        assertEquals("Cloudiness: 49%, Precipitations: 1.0 mm/h", text);
        //assertTrue(text.contains("49%"));
        //assertTrue(text.contains("1"));
    }

    JSONObject readJSON(String resourceName) throws IOException, JSONException {
        Reader reader = new InputStreamReader(getClass().getResourceAsStream(resourceName));
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
