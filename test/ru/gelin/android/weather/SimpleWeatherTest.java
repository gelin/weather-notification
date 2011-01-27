package ru.gelin.android.weather;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Date;

import org.junit.Test;

public class SimpleWeatherTest {
    
    @Test
    public void testIsEmpty() {
        SimpleWeather weather = new SimpleWeather();
        assertTrue(weather.isEmpty());
        weather.time = new Date();
        assertTrue(weather.isEmpty());
        weather.conditions = new ArrayList<WeatherCondition>();
        assertTrue(weather.isEmpty());
        weather.conditions.add(new SimpleWeatherCondition());
        assertFalse(weather.isEmpty()); //????
    }
    
    @Test
    public void testNullConditions() {
        SimpleWeather weather = new SimpleWeather();
        assertNotNull(weather.getConditions());
        weather.setConditions(null);
        assertNotNull(weather.getConditions());
        assertEquals(0, weather.getConditions().size());
        assertTrue(weather.isEmpty());
    }

}
