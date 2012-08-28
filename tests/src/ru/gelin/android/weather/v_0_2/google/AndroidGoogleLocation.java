/*
 *  Weather API.
 *  Copyright (C) 2010  Denis Nelubin aka Gelin
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

package ru.gelin.android.weather.v_0_2.google;

import android.location.Address;
import ru.gelin.android.weather.Location;

/**
 *  Wrapper for Android location to query Google weather API with geo coordinates.
 */
public class AndroidGoogleLocation implements Location {

    /** Query template */
    static final String QUERY = "%s,%s,%s,%d,%d";
    
    /** Android location */
    android.location.Location location;
    /** Android address */
    Address address;
    
    /**
     *  Creates the location from Android location.
     */
    public AndroidGoogleLocation(android.location.Location location) {
        this.location = location;
    }
    
    /**
     *  Creates the location from Android location and address.
     */
    public AndroidGoogleLocation(android.location.Location location, Address address) {
        this.location = location;
        this.address = address;
    }
    
    /**
     *  Creates the query with geo coordinates. 
     *  For example: ",,,30670000,104019996"
     */
    //@Override
    public String getQuery() {
        if (location == null) {
            return "";
        }
        if (address == null) {
            return String.format(QUERY,
                    "", "", "",
                    convertGeo(location.getLatitude()),
                    convertGeo(location.getLongitude()));
        }
        return String.format(QUERY,
                stringOrEmpty(address.getLocality()),
                stringOrEmpty(address.getAdminArea()),
                stringOrEmpty(address.getCountryName()),
                convertGeo(location.getLatitude()),
                convertGeo(location.getLongitude()));
    }

    //@Override
    public String getText() {
        if (location == null) {
            return "";
        }
        if (address == null) {
            return android.location.Location.convert(location.getLatitude(), 
                    android.location.Location.FORMAT_DEGREES) +
                    " " +
                    android.location.Location.convert(location.getLongitude(), 
                            android.location.Location.FORMAT_DEGREES);
        }
        StringBuilder result = new StringBuilder();
        result.append(stringOrEmpty(address.getLocality()));
        if (result.length() > 0) {
            result.append(", ");
        }
        result.append(stringOrEmpty(address.getAdminArea()));
        if (result.length() == 0) {
            result.append(stringOrEmpty(address.getCountryName()));
        }
        return result.toString();
    }
    
    //@Override
    public boolean isEmpty() {
        return this.location == null;
    }

    @Override
    public boolean isGeo() {
        return true;
    }

    int convertGeo(double geo) {
        return (int)(geo * 1000000);
    }
    
    String stringOrEmpty(String string) {
        if (string == null) {
            return "";
        }
        return string;
    }

}
