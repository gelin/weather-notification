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
import ru.gelin.android.weather.Wind;
import ru.gelin.android.weather.WindDirection;
import ru.gelin.android.weather.WindSpeedUnit;

public class GoogleWindTest extends AndroidTestCase {
    
    public void testParseText() {
        GoogleWind wind = new GoogleWind();
        wind.parseText("Wind: SW at 2 mph");
        assertEquals(WindSpeedUnit.MPH, wind.getSpeedUnit());
        assertEquals(2, wind.getSpeed());
        assertEquals(WindDirection.SW, wind.getDirection());
        assertEquals("Wind: SW at 2 mph", wind.getText());
    }
    
    public void testParseText2() {
        GoogleWind wind = new GoogleWind();
        wind.parseText("Wind: SSW at 42 mph");
        assertEquals(WindSpeedUnit.MPH, wind.getSpeedUnit());
        assertEquals(42, wind.getSpeed());
        assertEquals(WindDirection.SSW, wind.getDirection());
        assertEquals("Wind: SSW at 42 mph", wind.getText());
    }
    
    public void testParseTextWrong() {
        GoogleWind wind = new GoogleWind();
        wind.parseText("Wind: Is it a wind?");
        assertEquals(WindSpeedUnit.MPH, wind.getSpeedUnit());
        assertEquals(Wind.UNKNOWN, wind.getSpeed());
        assertEquals("Wind: Is it a wind?", wind.getText());
    }
    
    public void testParseTextWrongDirection() {
        GoogleWind wind = new GoogleWind();
        wind.parseText("Wind: XYZ at 42 mph");
        assertEquals(WindSpeedUnit.MPH, wind.getSpeedUnit());
        assertEquals(Wind.UNKNOWN, wind.getSpeed());
        assertEquals("Wind: XYZ at 42 mph", wind.getText());
    }
    
    public void testParseTextFloatSpeed() {
        GoogleWind wind = new GoogleWind();
        wind.parseText("Wind: SW at 3.14 mph");
        assertEquals(WindSpeedUnit.MPH, wind.getSpeedUnit());
        assertEquals(3, wind.getSpeed());
        assertEquals(WindDirection.SW, wind.getDirection());
        assertEquals("Wind: SW at 3.14 mph", wind.getText());
    }
    
}