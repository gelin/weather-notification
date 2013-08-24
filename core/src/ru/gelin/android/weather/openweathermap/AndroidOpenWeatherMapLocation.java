/*
 *  Weather API.
 *  Copyright (C) 2012  Denis Nelubin aka Gelin
 *
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 *
 *  http://gelin.ru
 *  mailto:den@gelin.ru
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
