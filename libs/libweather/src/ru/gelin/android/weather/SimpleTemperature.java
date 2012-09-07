/*
 *  Weather API.
 *  Copyright (C) 2010  Denis Nelubin, Vladimir Kubyshev
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
 *  Holds temperature values.
 *  Allows to set the values in different units.
 *  Automatically calculates high and low temps when current temp is set.
 *  Automatically calculates current temp when high and low are set.
 */
public class SimpleTemperature implements Temperature {

    TemperatureUnit tunit;
    int current = UNKNOWN;
    int low = UNKNOWN;
    int high = UNKNOWN;

    @Deprecated
    public SimpleTemperature(UnitSystem unit) {
        this.tunit = TemperatureUnit.valueOf(unit);
    }

    public SimpleTemperature(TemperatureUnit unit) {
        this.tunit = unit;
    }

    /**
     *  Sets the current temperature in specified unit system.
     */
    @Deprecated
    public void setCurrent(int temp, UnitSystem unit) {
        setCurrent(temp, TemperatureUnit.valueOf(unit));
    }

    /**
     *  Sets the low temperature in specified unit system.
     */
    @Deprecated
    public void setLow(int temp, UnitSystem unit) {
        setLow(temp, TemperatureUnit.valueOf(unit));
    }

    /**
     *  Sets the high temperature in specified unit system.
     */
    @Deprecated
    public void setHigh(int temp, UnitSystem unit) {
        setHigh(temp, TemperatureUnit.valueOf(unit));
    }

    /**
     *  Sets the current temperature in specified unit system.
     */
    public void setCurrent(int temp, TemperatureUnit unit) {
        if (temp == UNKNOWN) {
            this.current = UNKNOWN;
            return;
        }
        this.current = convertValue(temp, unit);
    }

    /**
     *  Sets the low temperature in specified unit system.
     */
    public void setLow(int temp, TemperatureUnit unit) {
        if (temp == UNKNOWN) {
            this.low = UNKNOWN;
            return;
        }
        this.low = convertValue(temp, unit);
    }

    /**
     *  Sets the high temperature in specified unit system.
     */
    public void setHigh(int temp, TemperatureUnit unit) {
        if (temp == UNKNOWN) {
            this.high = UNKNOWN;
            return;
        }
        this.high = convertValue(temp, unit);
    }

    //@Override
    public int getCurrent() {
        if (this.current == UNKNOWN) {
            if (getLow() == UNKNOWN || getHigh() == UNKNOWN) {
                return UNKNOWN;
            }
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
    @Deprecated
    public UnitSystem getUnitSystem() {
        switch (this.tunit) {
        case C: 
            return UnitSystem.SI;
        case F: 
            return UnitSystem.US;
        }
        return UnitSystem.SI;
    }

    //@Override
    public TemperatureUnit getTemperatureUnit() {
        return this.tunit;
    }

    /**
     *  Creates new temperature in another unit system.
     */
    @Deprecated
    public SimpleTemperature convert(UnitSystem unit) {
        return convert(TemperatureUnit.valueOf(unit));
    }

    public SimpleTemperature convert(TemperatureUnit unit) {
        SimpleTemperature result = new SimpleTemperature(unit);
        result.setCurrent(this.getCurrent(), this.getTemperatureUnit());
        result.setLow(this.getLow(), this.getTemperatureUnit());
        result.setHigh(this.getHigh(), this.getTemperatureUnit());
        return result;
    }

    /**
     *  Converts the value from provided unit system into this temperature set unit system.
     */
    protected int convertValue(int value, TemperatureUnit unit) {
        if (this.tunit.equals(unit)) {
            return value;
        }
        switch (unit) {
            case C:
                switch (this.tunit) {
                    case F:
                        return Math.round(value * 9f / 5f + 32);    //C -> F
                    case K:
                        return Math.round(value + 273.15f);     //C -> K
                }
            case F:
                switch (this.tunit) {
                    case C:
                        return Math.round((value - 32) * 5f / 9f);  //F -> C
                    case K:
                        return Math.round((value - 32) * 5f / 9f + 273.15f);  //F -> K
                }
            case K:
                switch (this.tunit) {
                    case C:
                        return Math.round(value - 273.15f);     //K -> C
                    case F:
                        return Math.round((value - 273.15f) * 9f / 5f + 32);    //K -> F
                }
        }
        return value;
    }
    
    @Override
    public String toString() {
        return this.getClass().getName() + 
            " unit: " + this.getTemperatureUnit() + 
            " current: " + this.getCurrent() +
            " high: " + this.getHigh() +
            " low: " + this.getLow();
    }
    
}
