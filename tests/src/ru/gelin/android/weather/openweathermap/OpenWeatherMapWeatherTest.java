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
