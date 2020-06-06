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

package ru.gelin.android.weather.openweathermap;

import org.junit.Test;

import static org.junit.Assert.*;

public class NameOpenWeatherMapLocationTest {

    @Test
    public void testNullLocation() {
        NameOpenWeatherMapLocation location = new NameOpenWeatherMapLocation("test");
        assertFalse(location.isGeo());
        assertEquals("test", location.getText());
        assertEquals("q=test", location.getQuery());
    }

    @Test
    public void testLatLonLocation1() {
        NameOpenWeatherMapLocation location = new NameOpenWeatherMapLocation("55,73");
        assertTrue(location.isGeo());
        assertEquals("55,73", location.getText());
        assertEquals("lat=55&lon=73", location.getQuery());
    }

    @Test
    public void testLatLonLocation2() {
        NameOpenWeatherMapLocation location = new NameOpenWeatherMapLocation("55.0 , 73.4");
        assertTrue(location.isGeo());
        assertEquals("55.0 , 73.4", location.getText());
        assertEquals("lat=55.0&lon=73.4", location.getQuery());
    }

    @Test
    public void testLatLonLocation3() {
        NameOpenWeatherMapLocation location = new NameOpenWeatherMapLocation("55.0  73.4");
        assertTrue(location.isGeo());
        assertEquals("55.0  73.4", location.getText());
        assertEquals("lat=55.0&lon=73.4", location.getQuery());
    }

    @Test
    public void testLatLonLocation4() {
        NameOpenWeatherMapLocation location = new NameOpenWeatherMapLocation("-55.0,-73.4");
        assertTrue(location.isGeo());
        assertEquals("-55.0,-73.4", location.getText());
        assertEquals("lat=-55.0&lon=-73.4", location.getQuery());
    }

    @Test
    public void testLatLonLocation5() {
        NameOpenWeatherMapLocation location = new NameOpenWeatherMapLocation("Omsk 55.0,73.4");
        assertTrue(location.isGeo());
        assertEquals("Omsk 55.0,73.4", location.getText());
        assertEquals("lat=55.0&lon=73.4", location.getQuery());
    }

}
