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

package ru.gelin.android.weather.notification.app;

/**
 *  Constants for preference keys.
 */
public class PreferenceKeys {

    private PreferenceKeys() {
        //avoid instantiation
    }

    /** Preference key for special preference which displays the weather */
    public static final String WEATHER = "weather";

    /** Refresh interval preference key */
    public static final String REFRESH_INTERVAL = "refresh_interval";
    /** Refresh interval default value */
    public static final String REFRESH_INTERVAL_DEFAULT = RefreshInterval.REFRESH_1H.toString();
    
    /** Location type preferences key */
    static final String LOCATION_TYPE = "location_type";
    /** Location type default value */
    static final String LOCATION_TYPE_DEFAULT = LocationType.LOCATION_NETWORK.toString();
    
    /** Manual location preferences key */
    static final String LOCATION = "location";
    /** Manual location default value */
    static final String LOCATION_DEFAULT = "";
    
    /** Skins preferences category */
    static final String SKINS_CATEGORY = "skins_category";
    /** Skins install preferences key */
    static final String SKINS_INSTALL = "skins_install";

    /** API Debug preference key */
    static final String API_DEBUG = "api_debug";
    /** API Debug default value */
    static final boolean API_DEBUG_DEFAULT = false;

}
