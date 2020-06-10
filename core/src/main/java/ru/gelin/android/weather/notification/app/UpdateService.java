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

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
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

import java.lang.ref.WeakReference;
import java.util.Date;

import static ru.gelin.android.weather.notification.AppUtils.EXTRA_FORCE;
import static ru.gelin.android.weather.notification.AppUtils.EXTRA_VERBOSE;
import static ru.gelin.android.weather.notification.PreferenceKeys.ENABLE_NOTIFICATION;
import static ru.gelin.android.weather.notification.PreferenceKeys.ENABLE_NOTIFICATION_DEFAULT;
import static ru.gelin.android.weather.notification.app.PermissionNotifications.ACCESS_LOCATION_NOTIFICATION;
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

    /** ID of the job */
    static final int JOB_ID = 1;
    /** Estimated job upload bytes */
    static final long JOB_UPLOAD_BYTES = 256;
    /** Estimated job download bytes */
    static final long JOB_DOWNLOAD_BYTES = 4 * 1024;
    /** Estimated job duration */
    static final long JOB_DURATION = 10000;
    /** Day duration */
    static final long DAY_DURATION = 24 * 60 * 60 * 1000;

    /**
     *  Lock used when maintaining update thread.
     */
    private static final Object staticLock = new Object();
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
                this.force = intent.getBooleanExtra(EXTRA_FORCE,
                        intent.hasExtra(LocationManager.KEY_LOCATION_CHANGED)); // force weather update if location update came
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

    @Override
    public void run() {
        SharedPreferences preferences =
            PreferenceManager.getDefaultSharedPreferences(this);

        Location location;
        LocationType locationType = getLocationType();
        if (LocationType.LOCATION_MANUAL.equals(locationType)) {
            location = createSearchLocation(preferences.getString(LOCATION, LOCATION_DEFAULT));
        } else {
            location = queryLocation(locationType);
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
    final Handler internalHandler = new UpdateHandler(new WeakReference<>(this));

    static class UpdateHandler extends Handler {

        private final WeakReference<UpdateService> serviceRef;

        public UpdateHandler(WeakReference<UpdateService> serviceRef) {
            this.serviceRef = serviceRef;
        }

        @Override
        public void handleMessage(Message msg) {
            synchronized(staticLock) {
                threadRunning = false;
            }

            UpdateService service = this.serviceRef.get();
            if (service == null) {
                return;
            }
            WeatherStorage storage = new WeatherStorage(service);
            synchronized(this.serviceRef) {
                switch (msg.what) {
                    case SUCCESS:
                        Log.i(TAG, "received weather: " +
                                service.weather.getLocation().getText() + " " + service.weather.getTime());
                        if (service.weather.isEmpty()) {
                            storage.updateTime();
                        } else {
                            storage.save(service.weather);  //saving only non-empty weather
                        }
                        service.scheduleNextRun(service.weather.getTime().getTime());
                        if (service.verbose && service.weather.isEmpty()) {
                            Toast.makeText(service,
                                    service.getString(R.string.weather_update_empty, service.location.getText()),
                                    Toast.LENGTH_LONG).show();
                        }
                        break;
                    case FAILURE:
                        Log.w(TAG, "failed to update weather", service.updateError);
                        storage.updateTime();
                        if (service.verbose) {
                            Toast.makeText(service,
                                    service.getString(R.string.weather_update_failed, service.updateError.getMessage()),
                                    Toast.LENGTH_LONG).show();
                        }
                        break;
                    case UNKNOWN_LOCATION:
                        Log.w(TAG, "failed to get location");
                        storage.updateTime();
                        if (service.verbose) {
                            Toast.makeText(service,
                                    service.getString(R.string.weather_update_unknown_location),
                                    Toast.LENGTH_LONG).show();
                        }
                        break;
                    case QUERY_LOCATION:
                        Log.d(TAG, "quering new location");
                        //storage.updateTime();     //don't signal about update
                        break;
                }
            }
            WeatherNotificationManager.update(service);
            service.stopSelf();
        }
    }

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

        JobInfo jobInfo = getJobInfo(lastUpdate);

        boolean notificationEnabled = PreferenceManager.getDefaultSharedPreferences(this).
                getBoolean(ENABLE_NOTIFICATION, ENABLE_NOTIFICATION_DEFAULT);

        JobScheduler jobScheduler = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
        if (notificationEnabled) {
            Log.i(TAG, "scheduling update to " + new Date(now + jobInfo.getMinLatencyMillis()));
            jobScheduler.schedule(jobInfo);
        } else {
            Log.d(TAG, "cancelling update schedule");
            jobScheduler.cancelAll();
        }
    }

    JobInfo getJobInfo(long lastUpdate) {
        long now = System.currentTimeMillis();
        RefreshInterval interval = getRefreshInterval();
        long nextUpdate = lastUpdate + interval.getInterval();
        if (nextUpdate <= now) {
            nextUpdate = now + interval.getInterval();
        }

        long delay = nextUpdate - now;

        JobInfo.Builder builder = new JobInfo.Builder(JOB_ID, new ComponentName(this, UpdateServiceJob.class));
        builder
            .setMinimumLatency(delay)
            .setOverrideDeadline(delay + DAY_DURATION)
            .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
            .setRequiresBatteryNotLow(true);
        if (Build.VERSION.SDK_INT >= 28) {
            builder
                .setEstimatedNetworkBytes(JOB_DOWNLOAD_BYTES, JOB_UPLOAD_BYTES)
                .setPrefetch(true);
        }
        return builder.build();
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
    Location queryLocation(LocationType locationType) {
        LocationManager manager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        if (manager == null) {
            return null;
        }

        LocationType allowedLocationType = checkAndRequestPermission(locationType);

        if (allowedLocationType.isPermissionGranted(this)) {

            String locationProvider = allowedLocationType.getLocationProvider();
            android.location.Location androidLocation =
                manager.getLastKnownLocation(locationProvider);

            if (androidLocation != null && !isExpired(androidLocation.getTime())) {
                return new AndroidOpenWeatherMapLocation(androidLocation);  //actual location
            }

            Location location = androidLocation == null ? null :
                new AndroidOpenWeatherMapLocation(androidLocation);     //expired location, if exists

            if (allowedLocationType.isProviderEnabled(this)) {
                try {
                    Log.d(TAG, "requesting location update from " + locationProvider);
                    manager.requestLocationUpdates(locationProvider,
                        0, 0, getPendingIntent(this.startIntent));  //try to update immediately
                    return location;
                } catch (IllegalArgumentException e) {  //no location provider
                    return location;
                }
            }

            return location;

        }

        return null;
    }

    /**
     * Checks is the permission of the specified location type is granted, asks for it if necessary.
     * @param locationType the desired location type
     * @return the actual location type which is allowed to perform
     */
    LocationType checkAndRequestPermission(LocationType locationType) {
        if (locationType.isPermissionGranted(this)) {
            return locationType;
        }
        LocationType lowerLocationType = locationType.getLowerPermissionType();
        if (lowerLocationType == null) {
            return locationType;
        }
        if (lowerLocationType.isPermissionGranted(this)) {
            return lowerLocationType;
        }

        displayPermissionNotification();

        return checkAndRequestPermission(lowerLocationType);
    }

    void displayPermissionNotification() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);

        builder.setSmallIcon(R.drawable.status_icon);
        builder.setContentTitle(getString(R.string.permission_required));
        builder.setContentText(getString(R.string.permission_required_details));

        builder.setWhen(System.currentTimeMillis());
        builder.setOngoing(false);
        builder.setAutoCancel(true);

        Intent intent = new Intent(this, MainActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        intent.addFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
//        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        builder.setContentIntent(PendingIntent.getActivity(this, 0, intent, 0));

        //Lollipop notification on lock screen
        builder.setVisibility(NotificationCompat.VISIBILITY_PRIVATE);

        Notification notification = builder.build();

        NotificationManager manager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(ACCESS_LOCATION_NOTIFICATION, notification);
    }

    /**
     *  Creates the location to search the location by the entered string.
     */
    Location createSearchLocation(String query) {
        return new NameOpenWeatherMapLocation(query);
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
