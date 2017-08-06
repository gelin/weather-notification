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
import ru.gelin.android.weather.source.HttpWeatherSource;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 *  Wrapper for a text query to ask OpenWeatherMap.org API by the city name.
 *  The lat and lon parameters are passed to API for tracking purposes.
 */
public class NameOpenWeatherMapLocation implements Location {

    /** Query template */
    static final String QUERY = "q=%s";  //q=omsk
    /** Query template */
    static final String QUERY_WITH_LOCATION = "q=%s&lat=%s&lon=%s";  //q=omsk&lat=54.96&lon=73.38

    /** Name */
    String name;
    /** Android location */
    android.location.Location location;

    /**
     *  Creates the location for the name and android location
     */
    public NameOpenWeatherMapLocation(String name, android.location.Location location) {
        this.name = name;
        this.location = location;
    }

    /**
     *  Creates the query with name.
     */
    //@Override
    public String getQuery() {
        try {
            if (this.location == null) {
                return String.format(QUERY,
                        URLEncoder.encode(this.name, HttpWeatherSource.ENCODING));
            } else {
                return String.format(QUERY_WITH_LOCATION,
                    URLEncoder.encode(this.name, HttpWeatherSource.ENCODING),
                    String.valueOf(location.getLatitude()),
                    String.valueOf(location.getLongitude()));
            }
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);  //never happens
        }
    }

    //@Override
    public String getText() {
        return this.name;
    }
    
    //@Override
    public boolean isEmpty() {
        return this.name == null;
    }

    @Override
    public boolean isGeo() {
        return false;
    }

}
