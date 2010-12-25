package ru.gelin.android.weather;

import java.util.Locale;

/**
 *  Source of the weather forecasts.
 */
public interface WeatherSource {

    /**
     *  Searches the weather for specified location.
     *  @throws WeatherException if the weather cannot be found
     */
    Weather query(Location location) throws WeatherException;
    
    /**
     *  Searches the weather for specified location and locale.
     *  @throws WeatherException if the weather cannot be found
     */
    Weather query(Location location, Locale locale) throws WeatherException;
    
}
