/*
 *  Android Weather Notification.
 *  Copyright (C) 2011  Denis Nelubin aka Gelin
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

package ru.gelin.android.weather.notification.skin.impl;

import ru.gelin.android.weather.Temperature;

public class TemperatureFormat {
	
    public String format(int temp) {
        if (temp == Temperature.UNKNOWN) {
            return "";
        }
        return signedValue(temp) + "\u00B0";
    }
	
    public String format(int temp, TemperatureType unit) {
        if (temp == Temperature.UNKNOWN) {
            return "";
        }
        switch (unit) {
        case C:
            return signedValue(temp) + "\u00B0C";
        case F:
            return signedValue(temp) + "\u00B0F";
        case CF:
            return signedValue(temp) + "\u00B0C";
        case FC:
            return signedValue(temp) + "\u00B0F";
        }
        return "";
    }
    
    public String format(int tempC, int tempF, TemperatureType unit) {
        if (tempC == Temperature.UNKNOWN || tempF == Temperature.UNKNOWN) {
            return "";
        }
        switch (unit) {
        case C:
            return signedValue(tempC) + "\u00B0C";
        case F:
            return signedValue(tempF) + "\u00B0F";
        case CF:
            return signedValue(tempC) + "\u00B0C(" + signedValue(tempF) + "\u00B0F)";
        case FC:
            return signedValue(tempF) + "\u00B0F(" + signedValue(tempC) + "\u00B0C)";
        }
        return "";
    }
    
    /**
     *  Returns the int value with a sign.
     *  This implementation returns only minus sign for negative values.
     */
    protected String signedValue(int value) {
        return String.valueOf(value);
    }

}
