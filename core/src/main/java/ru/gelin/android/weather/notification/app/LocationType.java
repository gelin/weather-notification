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
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.support.v4.content.ContextCompat;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Location types.
 * Location can be defined by GPS (fine), by Wi-Fi or cellular network signal (coarse) or manually (search string).
 */
public enum LocationType {

    LOCATION_MANUAL(null, null, PermissionRequests.NULL_REQUEST, null),
    LOCATION_NULL(null, null, PermissionRequests.NULL_REQUEST, null),
    LOCATION_NETWORK(
        LocationManager.NETWORK_PROVIDER,
        Build.VERSION.SDK_INT >= 29
            ? new String[] { Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_BACKGROUND_LOCATION }
            : new String[] { Manifest.permission.ACCESS_COARSE_LOCATION },
        PermissionRequests.ACCESS_COARSE_LOCATION_REQUEST,
        LOCATION_NULL),
    LOCATION_GPS(
        LocationManager.GPS_PROVIDER,
        Build.VERSION.SDK_INT >= 29
            ? new String[] { Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_BACKGROUND_LOCATION }
            : new String[] { Manifest.permission.ACCESS_FINE_LOCATION },
        PermissionRequests.ACCESS_FINE_LOCATION_REQUEST,
        LOCATION_NETWORK);

    private String locationProvider;
    private String[] permissions;
    private int permissionRequest;
    private LocationType lowerPermissionType;

    private LocationType(String locationProvider,
                         String[] permissions,
                         int permissionRequest,
                         LocationType lowerPermissionType) {
        this.locationProvider = locationProvider;
        this.permissions = permissions;
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
     * Returns the permissions necessary to use this location provider.
     */
    public String[] getPermissions() {
        return this.permissions;
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
        String[] permissions = getPermissions();
        if (permissions == null) {
            return false;
        }

        for (String permission : permissions) {
            int permissionCheck = ContextCompat.checkSelfPermission(context, permission);
            if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    /**
     * Returns true if the same set of permissions are granted.
     * @param permissions the set of permissions received in {@link Activity#onRequestPermissionsResult(int, String[], int[])}
     * @param grantResults the set of results received in {@link Activity#onRequestPermissionsResult(int, String[], int[])}
     */
    public boolean isPermissionGranted(String[] permissions, int[] grantResults) {
        Set<String> requiredPermissions = new HashSet<>(Arrays.asList(getPermissions()));
        Set<String> receivedPermissions = new HashSet<>(Arrays.asList(permissions));
        if (!requiredPermissions.containsAll(receivedPermissions)) {
            return false;
        }

        for (int grant : grantResults) {
            if (grant != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

}
