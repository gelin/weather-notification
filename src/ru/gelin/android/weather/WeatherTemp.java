package ru.gelin.android.weather;

/**
 *  Contains current, high and low temperature values.
 */
public interface WeatherTemp {

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
    
}
