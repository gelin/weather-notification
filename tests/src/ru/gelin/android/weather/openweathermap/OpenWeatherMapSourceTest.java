package ru.gelin.android.weather.openweathermap;

import android.test.AndroidTestCase;
import org.json.JSONException;
import org.json.JSONObject;
import ru.gelin.android.weather.*;

public class OpenWeatherMapSourceTest extends AndroidTestCase {

    public void testQueryOmsk() throws WeatherException {
        WeatherSource source = new OpenWeatherMapSource();
        Location location = new SimpleLocation("lat=54.96&lon=73.38&cnt=1");
        Weather weather = source.query(location);
        assertNotNull(weather);
        assertFalse(weather.isEmpty());
    }

    public void testQueryJSON() throws WeatherException, JSONException {
        OpenWeatherMapSource source = new OpenWeatherMapSource();
        Location location = new SimpleLocation("lat=54.96&lon=73.38&cnt=1");
        JSONObject json = source.queryJSON(location);
        assertNotNull(json);
        assertEquals("Omsk", json.getJSONArray("list").getJSONObject(0).getString("name"));
    }

}
