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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *  Wrapper for a text query to ask OpenWeatherMap.org API by the city name.
 *  The lat and lon parameters are passed to API for tracking purposes.
 */
public class NameOpenWeatherMapLocation implements Location {

    /** Query template */
    static final String QUERY = "q=%s";  //q=omsk
    /** Query template */
    static final String QUERY_WITH_LOCATION = "lat=%s&lon=%s";  //lat=54.96&lon=73.38
    /** Lat,lon name template */
    static final Pattern LAT_LON_PATTERN = Pattern.compile("(-?\\d+\\.?\\d*)\\s*[,;]?\\s*(-?\\d+\\.?\\d*)");

    /** Name */
    String name;
    /** Query */
    String query;
    /** Does it location contains geo coordinates? */
    boolean isGeo = false;

    /**
     *  Creates the location for the name.
     *  The name can be a city name of "lat,lon" pair.
     */
    public NameOpenWeatherMapLocation(String name) {
        this.name = name;
        try {
            this.query = String.format(QUERY, URLEncoder.encode(this.name, HttpWeatherSource.ENCODING));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }

        if (name != null) {
            Matcher m = LAT_LON_PATTERN.matcher(name);
            if (m.find()) {
                this.isGeo = true;
                this.query = String.format(QUERY_WITH_LOCATION, m.group(1), m.group(2));
            }
        }
    }

    /**
     *  Creates the query with name or lat/lon.
     */
    @Override
    public String getQuery() {
        return this.query;
    }

    @Override
    public String getText() {
        return this.name;
    }

    @Override
    public boolean isEmpty() {
        return this.name == null;
    }

    @Override
    public boolean isGeo() {
        return isGeo;
    }

}
