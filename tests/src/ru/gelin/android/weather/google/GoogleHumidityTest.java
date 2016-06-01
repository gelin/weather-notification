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

package ru.gelin.android.weather.google;

import android.test.AndroidTestCase;
import ru.gelin.android.weather.Humidity;

public class GoogleHumidityTest  extends AndroidTestCase {
    
    public void testParseText() {
        GoogleHumidity humidity = new GoogleHumidity();
        humidity.parseText("Humidity: 48%");
        assertEquals(48, humidity.getValue());
        assertEquals("Humidity: 48%", humidity.getText());
    }
    
    public void testParseTextWrong() {
        GoogleHumidity humidity = new GoogleHumidity();
        humidity.parseText("Humidity: non-number");
        assertEquals(Humidity.UNKNOWN, humidity.getValue());
        assertEquals("Humidity: non-number", humidity.getText());
    }
    
    public void testParseTextFloat() {
        GoogleHumidity humidity = new GoogleHumidity();
        humidity.parseText("Humidity: 3.14%");
        assertEquals(3, humidity.getValue());
        assertEquals("Humidity: 3.14%", humidity.getText());
    }
    
}