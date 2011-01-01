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
    UnitSystem getUnitSystem();
    
}
