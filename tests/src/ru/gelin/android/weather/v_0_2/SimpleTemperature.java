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

package ru.gelin.android.weather.v_0_2;

/**
 *  Holds temperature values.
 *  Allows to set the values in different unit systems.
 *  Automatically calculates high and low temps when current temp is set.
 *  Automatically calculates current temp when high and low are set.
 */
public class SimpleTemperature implements Temperature {

    UnitSystem unit;
    int current = UNKNOWN;
    int low = UNKNOWN;
    int high = UNKNOWN;
    
    /**
     *  Constructs the temperature.
     *  The stored values will be returned in the specified unit system.
     */
    public SimpleTemperature(UnitSystem unit) {
        this.unit = unit;
    }
    
    /**
     *  Sets the current temperature in specified unit system.
     */
    public void setCurrent(int temp, UnitSystem unit) {
        if (this.unit.equals(unit)) {
            this.current = temp;
        } else {
            this.current = convertValue(temp, unit);
        }
    }
    
    /**
     *  Sets the low temperature in specified unit system.
     */
    public void setLow(int temp, UnitSystem unit) {
        if (this.unit.equals(unit)) {
            this.low = temp;
        } else {
            this.low = convertValue(temp, unit);
        }
    }
    
    /**
     *  Sets the high temperature in specified unit system.
     */
    public void setHigh(int temp, UnitSystem unit) {
        if (this.unit.equals(unit)) {
            this.high = temp;
        } else {
            this.high = convertValue(temp, unit);
        }
    }
    
    //@Override
    public int getCurrent() {
        if (this.current == UNKNOWN) {
            return Math.round((getLow() + getHigh()) / 2f); 
        }
        return this.current;
    }

    //@Override
    public int getHigh() {
        if (this.high == UNKNOWN) {
            if (this.current == UNKNOWN) {
                return this.low;
            } else {
                return this.current;
            }
        }
        return this.high;
    }

    //@Override
    public int getLow() {
        if (this.low == UNKNOWN) {
            if (this.current == UNKNOWN) {
                return this.high;
            } else {
                return this.current;
            }
        }
        return this.low;
    }

    //@Override
    public UnitSystem getUnitSystem() {
        return this.unit;
    }
    
    /**
     *  Creates new temperature in another unit system.
     */
    public SimpleTemperature convert(UnitSystem unit) {
        SimpleTemperature result = new SimpleTemperature(unit);
        result.setCurrent(this.getCurrent(), this.getUnitSystem());
        result.setLow(this.getLow(), this.getUnitSystem());
        result.setHigh(this.getHigh(), this.getUnitSystem());
        return result;
    }
    
    /**
     *  Converts the value from provided unit system into this temperature set unit system.
     */
    int convertValue(int value, UnitSystem unit) {
        if (this.unit.equals(unit)) {
            return value;
        }   
        if (UnitSystem.SI.equals(unit)) {   //SI -> US
            return Math.round(value * 9f / 5f + 32);
        } else {    //US -> SI
            return Math.round((value - 32) * 5f / 9f);
        }
    }

}
