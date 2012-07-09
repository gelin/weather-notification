/*
 *  Weather API.
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
