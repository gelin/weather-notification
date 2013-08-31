package ru.gelin.android.weather.notification.app;

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

}
