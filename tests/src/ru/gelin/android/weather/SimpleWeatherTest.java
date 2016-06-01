/*
 * Copyright 2010—2016 Denis Nelubin and others.
 *
 * This file is part of Weather Notification.
 *
 * Weather Notification is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Weather Notification is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Weather Notification.  If not, see http://www.gnu.org/licenses/.
 */

package ru.gelin.android.weather;

import java.util.ArrayList;
import java.util.Date;

import android.test.AndroidTestCase;

public class SimpleWeatherTest  extends AndroidTestCase {
    
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
    
    public void testNullConditions() {
        SimpleWeather weather = new SimpleWeather();
        assertNotNull(weather.getConditions());
        weather.setConditions(null);
        assertNotNull(weather.getConditions());
        assertEquals(0, weather.getConditions().size());
        assertTrue(weather.isEmpty());
    }

}
