package ru.gelin.android.weather;

/**
 *  Identifies location to search weather.
 */
public interface Location {
    
    /**
     *  Returns the query String (for Google Weather API).
     */
    String getQuery();
    
    /**
     *  Returns the location in a human readable form.
     */
    String getText();
    
    /**
     *  Returns true if this location is empty 
     *  (i.e.) doesn't contains any useful data.
     */
    boolean isEmpty();

}
