package ru.gelin.android.weather;

import java.util.Date;
import java.util.List;

/**
 *  Simple weather realization. Just holds the values.
 */
public class SimpleWeather implements Weather {

    /** Location */
    Location location;
    /** Time */
    Date time;
    /** Unit system */
    UnitSystem unit;
    /** List of conditions */
    List<WeatherCondition> conditions;
    
    /**
     *  Sets the location.
     */
    public void setLocation(Location location) {
        this.location = location;
    }
    
    /**
     *  Sets the time.
     */
    public void setTime(Date time) {
        this.time = time;
    }
    
    /**
     *  Sets the unit system.
     */
    public void setUnitSystem(UnitSystem unit) {
        this.unit = unit;
    }
    
    /**
     *  Sets the weather conditions list.
     */
    public void setConditions(List<WeatherCondition> conditions) {
        this.conditions = conditions;
    }
    
    @Override
    public Location getLocation() {
        return this.location;
    }

    @Override
    public Date getTime() {
        return this.time;
    }

    @Override
    public UnitSystem getUnitSystem() {
        return this.unit;
    }
    
    @Override
    public List<WeatherCondition> getConditions() {
        return this.conditions;
    }

}
