package ru.gelin.android.weather;

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
    
    @Override
    public int getCurrent() {
        if (this.current == UNKNOWN) {
            return Math.round((getLow() + getHigh()) / 2); 
        }
        return this.current;
    }

    @Override
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

    @Override
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

    @Override
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
