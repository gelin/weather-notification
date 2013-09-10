/*
 *  Android Weather Notification.
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

package ru.gelin.android.weather.notification.app;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;
import ru.gelin.android.weather.Location;
import ru.gelin.android.weather.Weather;
import ru.gelin.android.weather.WeatherSource;
import ru.gelin.android.weather.notification.R;
import ru.gelin.android.weather.notification.WeatherStorage;
import ru.gelin.android.weather.notification.skin.WeatherNotificationManager;
import ru.gelin.android.weather.openweathermap.AndroidOpenWeatherMapLocation;
import ru.gelin.android.weather.openweathermap.NameOpenWeatherMapLocation;
import ru.gelin.android.weather.openweathermap.OpenWeatherMapSource;

import java.util.Date;

import static ru.gelin.android.weather.notification.AppUtils.EXTRA_FORCE;
import static ru.gelin.android.weather.notification.AppUtils.EXTRA_VERBOSE;
import static ru.gelin.android.weather.notification.PreferenceKeys.ENABLE_NOTIFICATION;
import static ru.gelin.android.weather.notification.PreferenceKeys.ENABLE_NOTIFICATION_DEFAULT;
import static ru.gelin.android.weather.notification.app.PreferenceKeys.*;
import static ru.gelin.android.weather.notification.app.Tag.TAG;

/**
 *  Service to update weather.
 *  Just start it. The new weather values will be wrote to SharedPreferences
 *  (use {@link ru.gelin.android.weather.notification.WeatherStorage} to extract them).
 */
public class UpdateService extends Service implements Runnable {

    /** Success update message */
    static final int SUCCESS = 0;
    /** Failure update message */
    static final int FAILURE = 1;
    /** Update message when location is unknown */
    static final int UNKNOWN_LOCATION = 2;
    /** Update message when querying new location */
    static final int QUERY_LOCATION = 3;
    
    /**
     *  Lock used when maintaining update thread.
     */
    private static Object staticLock = new Object();
    /**
     *  Flag if there is an update thread already running. We only launch a new
     *  thread if one isn't already running.
     */
    static boolean threadRunning = false;
    
    /** Verbose flag */
    boolean verbose = false;
    /** Force flag */
    boolean force = false;
    /** Queried location */
    Location location;
    /** Updated weather */
    Weather weather;
    /** Weather update error */
    Exception updateError;
    /** Intent which starts the service */
    Intent startIntent;

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        
        synchronized(this) {
            this.startIntent = intent;
            if (intent != null) {
                this.verbose = intent.getBooleanExtra(EXTRA_VERBOSE, false);
                this.force = intent.getBooleanExtra(EXTRA_FORCE, false);
            }
        }
        
        removeLocationUpdates();
        
        SharedPreferences preferences =
            PreferenceManager.getDefaultSharedPreferences(this);
        
        WeatherStorage storage = new WeatherStorage(UpdateService.this);
        Weather weather = storage.load();
        long lastUpdate = weather.getTime().getTime();
        boolean notificationEnabled = preferences.getBoolean(
                ENABLE_NOTIFICATION, ENABLE_NOTIFICATION_DEFAULT);
        
        scheduleNextRun(lastUpdate);
        
        synchronized(staticLock) {
            if (threadRunning) {
                return;     // only start processing thread if not already running
            }
            if (!force && !notificationEnabled) {
                skipUpdate(storage, "skipping update, notification disabled");
                return;
            }
            if (!force && !isExpired(lastUpdate)) {
                skipUpdate(storage, "skipping update, not expired");
                return;
            }
            if (!isNetworkAvailable()) {    //no network
                skipUpdate(storage, "skipping update, no network");
                if (verbose) {
                    Toast.makeText(UpdateService.this, 
                            getString(R.string.weather_update_no_network),
                            Toast.LENGTH_LONG).show();
                }
                return;
            }
            if (!threadRunning) {
                threadRunning = true;
                new Thread(this).start();
            }
        }
    }
    
    void skipUpdate(WeatherStorage storage, String logMessage) {
        stopSelf();
        Log.d(TAG, logMessage);
        storage.updateTime();
        WeatherNotificationManager.update(this);
    }
    
    //@Override
    public void run() {
        SharedPreferences preferences =
            PreferenceManager.getDefaultSharedPreferences(this);

        Location location = null;
        LocationType locationType = getLocationType();
        if (LocationType.LOCATION_MANUAL.equals(locationType)) {
            location = createSearchLocation(preferences.getString(LOCATION, LOCATION_DEFAULT));
        } else {
            location = queryLocation(locationType.getLocationProvider());
            if (location == null) {
                internalHandler.sendEmptyMessage(QUERY_LOCATION);
                return;
            }
        }
        synchronized(this) {
            this.location = location;
        }
        
        if (location == null || location.isEmpty()) {
            internalHandler.sendEmptyMessage(UNKNOWN_LOCATION);
            return;
        }
 
        WeatherSource source = new OpenWeatherMapSource(this);
        try {
            Weather weather = source.query(location);
            synchronized(this) {
                this.weather = weather;
            }
            internalHandler.sendEmptyMessage(SUCCESS);
        } catch (Exception e) {
            synchronized(this) {
                this.updateError = e;
            }
            internalHandler.sendEmptyMessage(FAILURE);
        }
    }
    
    /**
     *  Handles weather update result.
     */
    final Handler internalHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            synchronized(staticLock) {
                threadRunning = false;
            }
            
            WeatherStorage storage = new WeatherStorage(UpdateService.this);
            switch (msg.what) {
            case SUCCESS:
                synchronized(UpdateService.this) {
                    Log.i(TAG, "received weather: " +
                            weather.getLocation().getText() + " " + weather.getTime());
                    if (weather.isEmpty()) {
                        storage.updateTime();
                    } else {
                        storage.save(weather);  //saving only non-empty weather
                    }
                    scheduleNextRun(weather.getTime().getTime());
                    if (verbose && weather.isEmpty()) {
                        Toast.makeText(UpdateService.this,
                                getString(R.string.weather_update_empty, location.getText()), 
                                Toast.LENGTH_LONG).show();
                    }
                }
                break;
            case FAILURE:
                synchronized(UpdateService.this) {
                    Log.w(TAG, "failed to update weather", updateError);
                    storage.updateTime();
                    if (verbose) {
                        Toast.makeText(UpdateService.this, 
                                getString(R.string.weather_update_failed, updateError.getMessage()), 
                                Toast.LENGTH_LONG).show();
                    }
                }
                break;
            case UNKNOWN_LOCATION:
                synchronized(UpdateService.this) {
                    Log.w(TAG, "failed to get location");
                    storage.updateTime();
                    if (verbose) {
                        Toast.makeText(UpdateService.this, 
                                getString(R.string.weather_update_unknown_location), 
                                Toast.LENGTH_LONG).show();
                    }
                }
                break;
            case QUERY_LOCATION:
                synchronized(UpdateService.this) {
                    Log.d(TAG, "quering new location");
                    //storage.updateTime();     //don't signal about update
                }
                break;
            }
            WeatherNotificationManager.update(UpdateService.this);
            stopSelf();
        }
    };
    
    /**
     *  Check availability of network connections.
     *  Returns true if any network connection is available.
     */
    boolean isNetworkAvailable() {
        ConnectivityManager manager = 
            (ConnectivityManager)getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo info = manager.getActiveNetworkInfo();
        if (info == null) {
            return false;
        }
        return info.isAvailable();
    }
    
    /**
     *  Returns true if the last update time is expired.
     *  @param  timestamp  timestamp value to check
     */
    boolean isExpired(long timestamp) {
        long now = System.currentTimeMillis();
        RefreshInterval interval = getRefreshInterval();
        return timestamp + interval.getInterval() < now;
    }
    
    void scheduleNextRun(long lastUpdate) {
        long now = System.currentTimeMillis();
        RefreshInterval interval = getRefreshInterval();
        long nextUpdate = lastUpdate + interval.getInterval();
        if (nextUpdate <= now) {
            nextUpdate = now + interval.getInterval();
        }

        PendingIntent pendingIntent = getPendingIntent(null);   //don't inherit extra flags

        boolean notificationEnabled = PreferenceManager.getDefaultSharedPreferences(this).
                getBoolean(ENABLE_NOTIFICATION, ENABLE_NOTIFICATION_DEFAULT);
        
        AlarmManager alarmManager = (AlarmManager)getSystemService(
                Context.ALARM_SERVICE);
        if (notificationEnabled) {
            Log.d(TAG, "scheduling update to " + new Date(nextUpdate));
            alarmManager.set(AlarmManager.RTC, nextUpdate, pendingIntent);
        } else {
            Log.d(TAG, "cancelling update schedule");
            alarmManager.cancel(pendingIntent);
        }
    }
    
    RefreshInterval getRefreshInterval() {
        SharedPreferences preferences =
            PreferenceManager.getDefaultSharedPreferences(this);
        return RefreshInterval.valueOf(preferences.getString(
                REFRESH_INTERVAL, REFRESH_INTERVAL_DEFAULT));
    }

    LocationType getLocationType() {
        SharedPreferences preferences =
                PreferenceManager.getDefaultSharedPreferences(this);
        return LocationType.valueOf(preferences.getString(
                LOCATION_TYPE, LOCATION_TYPE_DEFAULT));
    }
    
    /**
     *  Queries current location using android services.
     */
    Location queryLocation(String locationProvider) {
        LocationManager manager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        if (manager == null) {
            return null;
        }
        android.location.Location androidLocation = 
                manager.getLastKnownLocation(locationProvider);

        if (androidLocation == null || isExpired(androidLocation.getTime())) {
            try {
                Log.d(TAG, "requested location update from " + locationProvider);
                manager.requestLocationUpdates(locationProvider,
                    0, 0, getPendingIntent(this.startIntent));  //try to update immediately 
                return null;
            } catch (IllegalArgumentException e) {
                return null;    //no location provider
            }
        }

        return new AndroidOpenWeatherMapLocation(androidLocation);
    }

    /**
     *  Creates the location to search the location by the entered string.
     */
    Location createSearchLocation(String query) {
        LocationManager manager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        if (manager == null) {
            return new NameOpenWeatherMapLocation(query, null);
        }
        android.location.Location androidLocation =
                manager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        return new NameOpenWeatherMapLocation(query, androidLocation);
    }

    /**
     *  Unsubscribes from location updates.
     */
    void removeLocationUpdates() {
        if (this.startIntent != null && this.startIntent.hasExtra(LocationManager.KEY_LOCATION_CHANGED)) {
            Log.d(TAG, "location updated");
        }
        LocationManager manager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        manager.removeUpdates(getPendingIntent(this.startIntent));
    }

    /**
     *  Returns pending intent to start the service.
     *  @param  intent to wrap into the pending intent or null
     */
    PendingIntent getPendingIntent(Intent intent) {
        Intent serviceIntent;
        if (intent == null) {
            serviceIntent = new Intent(this, UpdateService.class);
        } else {
            serviceIntent = new Intent(intent);
        }
        return PendingIntent.getService(this, 0, serviceIntent, PendingIntent.FLAG_UPDATE_CURRENT);
    }
    
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
