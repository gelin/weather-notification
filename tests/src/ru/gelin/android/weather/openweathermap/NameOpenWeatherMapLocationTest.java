package ru.gelin.android.weather.openweathermap;

import android.test.AndroidTestCase;
import ru.gelin.android.weather.WeatherException;

public class NameOpenWeatherMapLocationTest extends AndroidTestCase {

    public void testNullLocation() throws WeatherException {
        NameOpenWeatherMapLocation location = new NameOpenWeatherMapLocation("test", null);
        assertEquals("test", location.getText());
        assertEquals("q=test", location.getQuery());
    }

}
