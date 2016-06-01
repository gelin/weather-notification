/*
 * Copyright 2010—2016 Denis Nelubin and others.
 *
 * This file is part of Weather Notification.
 *
 * Weather Notification is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Weather Notification is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Weather Notification.  If not, see http://www.gnu.org/licenses/.
 */

package ru.gelin.android.weather;

import java.net.URL;
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
     *  Returns the time when the weather was requested.
     *  May differ from the weather timestamp.
     */
    Date getQueryTime();
    
    /**
     *  Returns default unit system (SI or US).
     */
    @Deprecated
    UnitSystem getUnitSystem();
    
    /**
     *  Returns weather conditions starting from the current weather 
     *  and following some forecasts.
     */
    List<WeatherCondition> getConditions();
    
    /**
     *  Returns true if this weather doesn't contains any actual values.
     */
    boolean isEmpty();

    /**
     *  Returns the URL to be opened in browser to display more detailed forecast.
     *  @return the URL or null
     */
    URL getForecastURL();

}
