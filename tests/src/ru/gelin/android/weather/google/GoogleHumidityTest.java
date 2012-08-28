/*
 *  Android Weather Notification.
 *  Copyright (C) 2011  Denis Nelubin
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