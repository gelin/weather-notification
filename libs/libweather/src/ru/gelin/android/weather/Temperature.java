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

/**
 *  Contains current, high and low temperature values.
 */
public interface Temperature {

    /** Unknown temperature value */
    static int UNKNOWN = Integer.MIN_VALUE;
    
    /**
     *  Current temperature.
     */
    int getCurrent();
    
    /**
     *  High forecast temperature.
     */
    int getHigh();
    
    /**
     *  Low forecast temperature.
     */
    int getLow();
    
    /**
     *  Units of this weather.
     */
    @Deprecated
    UnitSystem getUnitSystem();
    
    /**
     *  Units of this weather.
     */
    TemperatureUnit getTemperatureUnit();
    
}
