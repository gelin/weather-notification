package ru.gelin.android.weather;

/**
 *  Contains current, high and low temperature values.
 */
public interface Temperature {

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
    UnitSystem getUnit();
    
}
