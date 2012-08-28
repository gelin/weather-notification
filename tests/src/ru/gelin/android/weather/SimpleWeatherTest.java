/*
 *  Android Weather Notification.
 *  Copyright (C) 2010  Denis Nelubin aka Gelin
 *
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 *
 *  http://gelin.ru
 *  mailto:den@gelin.ru
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
