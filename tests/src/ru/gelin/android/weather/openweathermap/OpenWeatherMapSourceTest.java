package ru.gelin.android.weather.openweathermap;

import android.test.AndroidTestCase;
import org.json.JSONException;
import org.json.JSONObject;
import ru.gelin.android.weather.*;

public class OpenWeatherMapSourceTest extends AndroidTestCase {

    public void testQueryOmsk() throws WeatherException {
        WeatherSource source = new OpenWeatherMapSource(getContext());
        Location location = new SimpleLocation("lat=54.96&lon=73.38", true);
        Weather weather = source.query(location);
        assertNotNull(weather);
        assertFalse(weather.isEmpty());
    }

    public void testQueryOmskJSON() throws WeatherException, JSONException {
        OpenWeatherMapSource source = new OpenWeatherMapSource(getContext());
        Location location = new SimpleLocation("lat=54.96&lon=73.38", true);
        JSONObject json = source.queryCurrentWeather(location);
        assertNotNull(json);
        assertEquals("Omsk", json.getString("name"));
    }

    public void testQueryOmskName() throws WeatherException {
        WeatherSource source = new OpenWeatherMapSource(getContext());
        Location location = new SimpleLocation("q=omsk", false);
        Weather weather = source.query(location);
        assertNotNull(weather);
        assertFalse(weather.isEmpty());
    }

    public void testQueryOmskNameJSON() throws WeatherException, JSONException {
        OpenWeatherMapSource source = new OpenWeatherMapSource(getContext());
        Location location = new SimpleLocation("q=omsk", false);
        JSONObject json = source.queryCurrentWeather(location);
        assertNotNull(json);
        assertEquals("Omsk", json.getString("name"));
    }

    public void testQueryOmskForecasts() throws WeatherException {
        WeatherSource source = new OpenWeatherMapSource(getContext());
        Location location = new SimpleLocation("lat=54.96&lon=73.38&cnt=4", true);
        Weather weather = source.query(location);
        assertNotNull(weather);
        assertFalse(weather.isEmpty());
        assertEquals(4, weather.getConditions().size());
    }

    public void testQueryTestLocationPlus() throws WeatherException {
        WeatherSource source = new OpenWeatherMapSource(getContext());
        Location location = new SimpleLocation("+25", false);
        Weather weather = source.query(location);
        assertNotNull(weather);
        assertFalse(weather.isEmpty());
        assertEquals("Test location", weather.getLocation().getText());
        assertEquals(25, weather.getConditions().get(0).getTemperature(TemperatureUnit.C).getCurrent());
    }

    public void testQueryTestLocationMinus() throws WeatherException {
        WeatherSource source = new OpenWeatherMapSource(getContext());
        Location location = new SimpleLocation("-25", false);
        Weather weather = source.query(location);
        assertNotNull(weather);
        assertFalse(weather.isEmpty());
        assertEquals("Test location", weather.getLocation().getText());
        assertEquals(-25, weather.getConditions().get(0).getTemperature(TemperatureUnit.C).getCurrent());
    }

//    In API 2.5 the city name is not localized
//
//    public void testQueryOmskJSON_RU() throws WeatherException, JSONException {
//        OpenWeatherMapSource source = new OpenWeatherMapSource(getContext());
//        Location location = new SimpleLocation("lat=54.96&lon=73.38&lang=ru", true);
//        JSONObject json = source.queryCurrentWeather(location);
//        assertNotNull(json);
//        assertEquals("Омск", json.getString("name"));
//    }
//
//    public void testQueryOmskNameJSON_RU() throws WeatherException, JSONException {
//        OpenWeatherMapSource source = new OpenWeatherMapSource(getContext());
//        Location location = new SimpleLocation("q=omsk&lang=ru", false);
//        JSONObject json = source.queryCurrentWeather(location);
//        assertNotNull(json);
//        assertEquals("Омск", json.getString("name"));
//    }
//
//    public void testQueryOmskNameJSON_EO() throws WeatherException, JSONException {
//        OpenWeatherMapSource source = new OpenWeatherMapSource(getContext());
//        Location location = new SimpleLocation("q=omsk&lang=eo", false);    //unsupported language
//        JSONObject json = source.queryCurrentWeather(location);
//        assertNotNull(json);
//        assertEquals("Omsk", json.getString("name"));
//    }

}
