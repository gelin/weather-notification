package ru.gelin.android.weather;

/**
 *  Identifies location to search weather.
 */
public interface Location {
    
    /**
     *  Returns the query String (for Google Weather API).
     */
    String getQuery();

}
