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
