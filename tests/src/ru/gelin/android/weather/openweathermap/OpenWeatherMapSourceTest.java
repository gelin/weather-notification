package ru.gelin.android.weather.openweathermap;

import android.test.AndroidTestCase;
import org.json.JSONException;
import org.json.JSONObject;
import ru.gelin.android.weather.*;

public class OpenWeatherMapSourceTest extends AndroidTestCase {

    public void testQueryOmsk() throws WeatherException {
        WeatherSource source = new OpenWeatherMapSource();
        Location location = new SimpleLocation("lat=54.96&lon=73.38&cnt=1", true);
        Weather weather = source.query(location);
        assertNotNull(weather);
        assertFalse(weather.isEmpty());
    }

    public void testQueryOmskJSON() throws WeatherException, JSONException {
        OpenWeatherMapSource source = new OpenWeatherMapSource();
        Location location = new SimpleLocation("lat=54.96&lon=73.38&cnt=1", true);
        JSONObject json = source.queryCityWeather(location);
        assertNotNull(json);
        assertEquals("Omsk", json.getJSONArray("list").getJSONObject(0).getString("name"));
    }

    public void testQueryOmskName() throws WeatherException {
        WeatherSource source = new OpenWeatherMapSource();
        Location location = new SimpleLocation("q=omsk", false);
        Weather weather = source.query(location);
        assertNotNull(weather);
        assertFalse(weather.isEmpty());
    }

    public void testQueryOmskNameJSON() throws WeatherException, JSONException {
        OpenWeatherMapSource source = new OpenWeatherMapSource();
        Location location = new SimpleLocation("q=omsk", false);
        JSONObject json = source.queryCityWeather(location);
        assertNotNull(json);
        assertEquals("Omsk", json.getJSONArray("list").getJSONObject(0).getString("name"));
    }

    public void testQueryOmskForecasts() throws WeatherException {
        WeatherSource source = new OpenWeatherMapSource();
        Location location = new SimpleLocation("lat=54.96&lon=73.38&cnt=1", true);
        Weather weather = source.query(location);
        assertNotNull(weather);
        assertFalse(weather.isEmpty());
        assertEquals(4, weather.getConditions().size());
    }

}
