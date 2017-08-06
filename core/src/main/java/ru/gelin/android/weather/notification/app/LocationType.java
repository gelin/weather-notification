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

import android.content.Context;
import android.location.LocationManager;

/**
 *  Location types.
 *  Location can be defined by GPS (fine), by Wi-Fi or cellular network signal (coarse) or manually (search string).
 */
public enum LocationType {

    LOCATION_GPS(LocationManager.GPS_PROVIDER),
    LOCATION_NETWORK(LocationManager.NETWORK_PROVIDER),
    LOCATION_MANUAL(null);

    String locationProvider;

    private LocationType(String locationProvider) {
        this.locationProvider = locationProvider;
    }

    /**
     *  Returns the name of the Android location provider to use.
     */
    public String getLocationProvider() {
        return this.locationProvider;
    }

    /**
     *  Returns true if the location provider related to this location type is enabled.
     */
    public boolean isProviderEnabled(Context context) {
        LocationManager manager = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
        if (manager == null) {
            return false;
        }
        String provider = getLocationProvider();
        if (provider == null) {
            return true;
        }
        return manager.isProviderEnabled(provider);
    }

}
