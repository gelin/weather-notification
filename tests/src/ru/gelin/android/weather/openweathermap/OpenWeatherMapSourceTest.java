package ru.gelin.android.weather.openweathermap;

import android.test.AndroidTestCase;
import org.json.JSONException;
import ru.gelin.android.weather.*;

public class OpenWeatherMapSourceTest extends AndroidTestCase {

    public void testQueryOmsk() throws WeatherException {
        WeatherSource source = new OpenWeatherMapSource();
        Location location = new SimpleLocation("type=station&lat1=54.80&lat2=55.15&lng1=73.08&lng2=73.70");
        Weather weather = source.query(location);
        assertNotNull(weather);
        assertFalse(weather.isEmpty());
    }

    public void testQueryJSON() throws WeatherException, JSONException {
        OpenWeatherMapSource source = new OpenWeatherMapSource();
        Location location = new SimpleLocation("type=station&lat1=54.80&lat2=55.15&lng1=73.08&lng2=73.70");
        Weather weather = source.query(location);
        assertNotNull(source.json);
        assertEquals("Omsk", source.json.getJSONArray("list").getJSONObject(0).getString("name"));
    }

}
