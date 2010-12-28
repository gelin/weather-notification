package ru.gelin.android.weather;

import java.util.Date;
import java.util.List;

/**
 *  Interface which contains the weather query results.
 */
public interface Weather {
    
    /**
     *  Returns the weather location (original, passed to {@link WeatherSource#query},
     *  or modified by the weather source implementation)
     */
    Location getLocation();
    
    /**
     *  Returns the current weather timestamp.
     */
    Date getTime();
    
    /**
     *  Returns default unit system (SI or US).
     */
    UnitSystem getUnitSystem();
    
    /**
     *  Returns weather conditions starting from the current weather 
     *  and following some forecasts.
     */
    List<WeatherCondition> getConditions();

}
