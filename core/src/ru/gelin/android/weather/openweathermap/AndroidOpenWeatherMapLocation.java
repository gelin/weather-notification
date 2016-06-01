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

package ru.gelin.android.weather.openweathermap;

import ru.gelin.android.weather.Location;

/**
 *  Wrapper for Android location to query OpenWeatherMap.org API with geo coordinates.
 */
public class AndroidOpenWeatherMapLocation implements Location {

    /** Query template */
    static final String QUERY = "lat=%s&lon=%s";  //lat=54.96&lon=73.38

    /** Android location */
    android.location.Location location;

    /**
     *  Creates the location from Android location.
     */
    public AndroidOpenWeatherMapLocation(android.location.Location location) {
        this.location = location;
    }

    /**
     *  Creates the query with geo coordinates. 
     *  For example: "lat=54.96&lon=73.38"
     */
    //@Override
    public String getQuery() {
        if (location == null) {
            return "";
        }
        return String.format(QUERY,
                String.valueOf(location.getLatitude()),
                String.valueOf(location.getLongitude()));
    }

    //@Override
    public String getText() {
        if (location == null) {
            return "";
        }
        return android.location.Location.convert(location.getLatitude(),
                    android.location.Location.FORMAT_DEGREES) +
                    "," +
                    android.location.Location.convert(location.getLongitude(),
                    android.location.Location.FORMAT_DEGREES);
    }
    
    //@Override
    public boolean isEmpty() {
        return this.location == null;
    }

    @Override
    public boolean isGeo() {
        return true;
    }

}
