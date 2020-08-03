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

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.support.v4.content.ContextCompat;

/**
 * Location types.
 * Location can be defined by GPS (fine), by Wi-Fi or cellular network signal (coarse) or manually (search string).
 */
public enum LocationType {
    LOCATION_MANUAL(null, null, PermissionRequests.NULL_REQUEST, null),
    LOCATION_NULL(null, null, PermissionRequests.NULL_REQUEST, null),
    LOCATION_BACKGROUND(null,
        Manifest.permission.ACCESS_BACKGROUND_LOCATION,
        PermissionRequests.ACCESS_BACKGROUND_LOCATION_REQUEST,
        null),
    LOCATION_NETWORK(
        LocationManager.NETWORK_PROVIDER,
        Manifest.permission.ACCESS_COARSE_LOCATION,
        PermissionRequests.ACCESS_COARSE_LOCATION_REQUEST,
        LOCATION_NULL),
    LOCATION_GPS(
        LocationManager.GPS_PROVIDER,
        Manifest.permission.ACCESS_FINE_LOCATION,
        PermissionRequests.ACCESS_FINE_LOCATION_REQUEST,
        LOCATION_NETWORK);

    private String locationProvider;
    private String permission;
    private int permissionRequest;
    private LocationType lowerPermissionType;

    private LocationType(String locationProvider,
                         String permission,
                         int permissionRequest,
                         LocationType lowerPermissionType) {
        this.locationProvider = locationProvider;
        this.permission = permission;
        this.permissionRequest = permissionRequest;
        this.lowerPermissionType = lowerPermissionType;
    }

    /**
     * Returns the name of the Android location provider to use.
     */
    public String getLocationProvider() {
        return this.locationProvider;
    }

    /**
     * Returns the permission necessary to use this location provider.
     */
    public String getPermission() {
        return this.permission;
    }

    /**
     * Returns the permission request ID.
     */
    public int getPermissionRequest() {
        return this.permissionRequest;
    }

    /**
     * Returns the next possible location type to try.
     * @return the next type or null
     */
    public LocationType getLowerPermissionType() {
        return this.lowerPermissionType;
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

    /**
     * Returns true if usage of this location provider is permitted by the user.
     */
    public boolean isPermissionGranted(Context context) {
        String permission = getPermission();
        if (permission == null) {
            return false;
        }
        int permissionCheck = ContextCompat.checkSelfPermission(context, permission);
        return permissionCheck == PackageManager.PERMISSION_GRANTED;
    }

}
