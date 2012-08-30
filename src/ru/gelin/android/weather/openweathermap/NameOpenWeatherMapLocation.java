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
import ru.gelin.android.weather.source.HttpWeatherSource;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 *  Wrapper for Android location to query OpenWeatherMap.org API with geo coordinates.
 */
public class NameOpenWeatherMapLocation implements Location {

    /** Query template */
    static final String QUERY = "q=%s";  //q=omsk

    /** Name */
    String name;

    /**
     *  Creates the location for the name.
     */
    public NameOpenWeatherMapLocation(String name) {
        this.name = name;
    }

    /**
     *  Creates the query with name.
     */
    //@Override
    public String getQuery() {
        try {
            return String.format(QUERY,
                    URLEncoder.encode(this.name, HttpWeatherSource.ENCODING));
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
