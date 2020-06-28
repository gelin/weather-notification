package ru.gelin.android.weather.notification.app;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
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
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;
import ru.gelin.android.weather.Location;
import ru.gelin.android.weather.Weather;
import ru.gelin.android.weather.WeatherSource;
import ru.gelin.android.weather.notification.AppUtils;
import ru.gelin.android.weather.notification.R;
import ru.gelin.android.weather.notification.WeatherStorage;
import ru.gelin.android.weather.notification.skin.WeatherNotificationManager;
import ru.gelin.android.weather.openweathermap.AndroidOpenWeatherMapLocation;
import ru.gelin.android.weather.openweathermap.NameOpenWeatherMapLocation;
import ru.gelin.android.weather.openweathermap.OpenWeatherMapSource;
import ru.gelin.android.weather.openweathermap.TooManyRequestsException;

import java.lang.ref.WeakReference;
import java.util.Date;

import static android.content.Context.CONNECTIVITY_SERVICE;
import static ru.gelin.android.weather.notification.PreferenceKeys.ENABLE_NOTIFICATION;
import static ru.gelin.android.weather.notification.PreferenceKeys.ENABLE_NOTIFICATION_DEFAULT;
import static ru.gelin.android.weather.notification.app.PermissionNotifications.ACCESS_LOCATION_NOTIFICATION;
import static ru.gelin.android.weather.notification.app.PreferenceKeys.*;
import static ru.gelin.android.weather.notification.app.PreferenceKeys.LOCATION_DEFAULT;
import static ru.gelin.android.weather.notification.app.Tag.TAG;

/**
 * Incapsulates functionality to update the weather.
 */
class WeatherUpdater implements Runnable {

    /** ID of the job */
    static final int JOB_ID = 1;
    /** Estimated job upload bytes */
    static final long JOB_UPLOAD_BYTES = 256;
    /** Estimated job download bytes */
    static final long JOB_DOWNLOAD_BYTES = 4 * 1024;
    /** Estimated job duration */
    static final long JOB_DURATION = 10000;
    /** Day duration */
    static final long JOB_DELAY = 15 * 60 * 1000;       // 15 min

    /** Success update message */
    static final int SUCCESS = 0;
    /** Failure update message */
    static final int FAILURE = 1;
    /** Update message when location is unknown */
    static final int UNKNOWN_LOCATION = 2;
    /** Update message when querying new location */
    static final int QUERY_LOCATION = 3;
    /** Update message when API key failed */
    static final int API_KEY_FAILURE = 4;

    private final Context context;

    /** Lock used when maintaining update thread. */
    private static final Object staticLock = new Object();

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
    /** Complete callback */
    Runnable onCompleted = new Runnable() {
        @Override
        public void run() {
            // empty, but not null
        }
    };

    /**
     *  Flag if there is an update thread already running. We only launch a new
     *  thread if one isn't already running.
     */
    static boolean threadRunning = false;

    WeatherUpdater(Context context) {
        this.context = context;
    }

    /**
     * Starts weather update. Schedules next run.
     * Skips update if weather is not expired yet, or notifications are disabled, or network is not available.
     * Runs update in a separate thread.
     * When the update is completed it sends a message to the handler in the main thread.
     * The handler updates the weather storage and initiates notification update.
     * @param verbose if true, errors are displayed as toast messages
     * @param force if true, the update is started even for non expired weather and when notifications are disabled
     * @param onCompleted this callback is run when the update is completed (or skipped)
     */
    void update( boolean verbose, boolean force,Runnable onCompleted) {
        this.verbose = verbose;
        this.force = force;
        if (onCompleted != null) {
            this.onCompleted = onCompleted;
        }

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this.context);

        WeatherStorage storage = new WeatherStorage(this.context);
        Weather weather = storage.load();
        long lastUpdate = weather.getTime().getTime();
        boolean notificationEnabled = preferences.getBoolean(ENABLE_NOTIFICATION, ENABLE_NOTIFICATION_DEFAULT);

        scheduleNextRun(lastUpdate);

        synchronized(staticLock) {
            if (threadRunning) {
                onCompleted.run();
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
                    Toast.makeText(context,
                        context.getString(R.string.weather_update_no_network),
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

    void scheduleNextRun(long lastUpdate) {
        long now = System.currentTimeMillis();

        JobInfo jobInfo = getJobInfo(lastUpdate);

        boolean notificationEnabled = PreferenceManager.getDefaultSharedPreferences(context).
            getBoolean(ENABLE_NOTIFICATION, ENABLE_NOTIFICATION_DEFAULT);

        JobScheduler jobScheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
        if (notificationEnabled) {
            Log.d(TAG, "scheduling update to " + new Date(now + jobInfo.getMinLatencyMillis()));
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
//        delay = 2000; // 2 seconds for test

        JobInfo.Builder builder = new JobInfo.Builder(JOB_ID, new ComponentName(context, UpdateJobService.class));
        builder
            .setMinimumLatency(delay)
            .setOverrideDeadline(delay + JOB_DELAY)
            .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
            .setPersisted(true);
        if (Build.VERSION.SDK_INT >= 26) {
            builder
                .setRequiresBatteryNotLow(true);
        }
        if (Build.VERSION.SDK_INT >= 28) {
            builder
                .setEstimatedNetworkBytes(JOB_DOWNLOAD_BYTES, JOB_UPLOAD_BYTES)
                .setPrefetch(true);
        }
        return builder.build();
    }

    RefreshInterval getRefreshInterval() {
        SharedPreferences preferences =
            PreferenceManager.getDefaultSharedPreferences(context);
        return RefreshInterval.valueOf(preferences.getString(
            REFRESH_INTERVAL, REFRESH_INTERVAL_DEFAULT));
    }

    void skipUpdate(WeatherStorage storage, String logMessage) {
        Log.d(TAG, logMessage);
        storage.updateTime();
        WeatherNotificationManager.update(context);
        onCompleted.run();
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

    /**
     *  Check availability of network connections.
     *  Returns true if any network connection is available.
     */
    boolean isNetworkAvailable() {
        ConnectivityManager manager =
            (ConnectivityManager) context.getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo info = manager.getActiveNetworkInfo();
        if (info == null) {
            return false;
        }
        return info.isAvailable();
    }

    @Override
    public void run() {
        SharedPreferences preferences =
            PreferenceManager.getDefaultSharedPreferences(context);

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

        WeatherSource source = new OpenWeatherMapSource(context);
        try {
            Weather weather = source.query(location);
            synchronized (this) {
                this.weather = weather;
            }
            internalHandler.sendEmptyMessage(SUCCESS);
        } catch (TooManyRequestsException e) {
            synchronized(this) {
                this.updateError = e;
            }
            internalHandler.sendEmptyMessage(API_KEY_FAILURE);
        } catch (Exception e) {
            synchronized(this) {
                this.updateError = e;
            }
            internalHandler.sendEmptyMessage(FAILURE);
        }
    }

    LocationType getLocationType() {
        SharedPreferences preferences =
            PreferenceManager.getDefaultSharedPreferences(context);
        return LocationType.valueOf(preferences.getString(
            LOCATION_TYPE, LOCATION_TYPE_DEFAULT));
    }

    /**
     *  Creates the location to search the location by the entered string.
     */
    Location createSearchLocation(String query) {
        return new NameOpenWeatherMapLocation(query);
    }

    /**
     *  Queries current location using android services.
     */
    Location queryLocation(LocationType locationType) {
        LocationManager manager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        if (manager == null) {
            return null;
        }

        LocationType allowedLocationType = checkAndRequestPermission(locationType);

        if (allowedLocationType.isPermissionGranted(context)) {

            String locationProvider = allowedLocationType.getLocationProvider();
            android.location.Location androidLocation =
                manager.getLastKnownLocation(locationProvider);

            if (androidLocation != null && !isExpired(androidLocation.getTime())) {
                return new AndroidOpenWeatherMapLocation(androidLocation);  //actual location
            }

            Location location = androidLocation == null ? null :
                new AndroidOpenWeatherMapLocation(androidLocation);     //expired location, if exists

            if (allowedLocationType.isProviderEnabled(context)) {
                try {
                    Log.d(TAG, "requesting location update from " + locationProvider);
                    manager.requestLocationUpdates(locationProvider,
                        0, 0, getServicePendingIntent());  //try to update immediately
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
        if (locationType.isPermissionGranted(context)) {
            return locationType;
        }
        LocationType lowerLocationType = locationType.getLowerPermissionType();
        if (lowerLocationType == null) {
            return locationType;
        }
        if (lowerLocationType.isPermissionGranted(context)) {
            return lowerLocationType;
        }

        displayPermissionNotification();

        return checkAndRequestPermission(lowerLocationType);
    }

    void displayPermissionNotification() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);

        builder.setSmallIcon(R.drawable.status_icon);
        builder.setContentTitle(context.getString(R.string.permission_required));
        builder.setContentText(context.getString(R.string.permission_required_details));

        builder.setWhen(System.currentTimeMillis());
        builder.setOngoing(false);
        builder.setAutoCancel(true);

        Intent intent = new Intent(context, MainActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        intent.addFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
//        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        builder.setContentIntent(PendingIntent.getActivity(context, 0, intent, 0));

        //Lollipop notification on lock screen
        builder.setVisibility(NotificationCompat.VISIBILITY_PRIVATE);

        Notification notification = builder.build();

        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(ACCESS_LOCATION_NOTIFICATION, notification);
    }

    /**
     *  Returns pending intent to start the service.
     */
    PendingIntent getServicePendingIntent() {
        Intent serviceIntent;
        synchronized (this) {
            serviceIntent = AppUtils.getUpdateServiceIntent(context, verbose, force);
        }
        return PendingIntent.getService(context, 0, serviceIntent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    /**
     *  Handles weather update result.
     */
    final Handler internalHandler = new UpdateHandler(new WeakReference<>(this));

    static class UpdateHandler extends Handler {

        private final WeakReference<WeatherUpdater> serviceRef;

        public UpdateHandler(WeakReference<WeatherUpdater> serviceRef) {
            this.serviceRef = serviceRef;
        }

        @Override
        public void handleMessage(Message msg) {
            synchronized(staticLock) {
                threadRunning = false;
            }

            WeatherUpdater updater = this.serviceRef.get();
            if (updater == null) {
                return;
            }

            WeatherStorage storage = new WeatherStorage(updater.context);

            synchronized(this.serviceRef) {
                switch (msg.what) {
                    case SUCCESS:
                        Log.i(TAG, "received weather: " +
                            updater.weather.getLocation().getText() + " " + updater.weather.getTime());
                        if (updater.weather.isEmpty()) {
                            storage.updateTime();
                        } else {
                            storage.save(updater.weather);  //saving only non-empty weather
                        }
                        updater.scheduleNextRun(updater.weather.getTime().getTime());
                        if (updater.verbose && updater.weather.isEmpty()) {
                            Toast.makeText(updater.context,
                                updater.context.getString(R.string.weather_update_empty, updater.location.getText()),
                                Toast.LENGTH_LONG).show();
                        }
                        break;
                    case FAILURE:
                        Log.w(TAG, "failed to update weather", updater.updateError);
                        storage.updateTime();
                        if (updater.verbose) {
                            Toast.makeText(updater.context,
                                updater.context.getString(R.string.weather_update_failed, updater.updateError.getMessage()),
                                Toast.LENGTH_LONG).show();
                        }
                        break;
                    case API_KEY_FAILURE:
                        Log.w(TAG, "failed to request API", updater.updateError);
                        storage.updateTime();
                        if (updater.verbose) {
                            Toast.makeText(updater.context,
                                updater.context.getString(R.string.owm_api_key_failure_notice) + "\n\n" +
                                updater.context.getString(R.string.weather_update_failed, updater.updateError.getMessage()),
                                Toast.LENGTH_LONG).show();
                        }
                        break;
                    case UNKNOWN_LOCATION:
                        Log.w(TAG, "failed to get location");
                        storage.updateTime();
                        if (updater.verbose) {
                            Toast.makeText(updater.context,
                                updater.context.getString(R.string.weather_update_unknown_location),
                                Toast.LENGTH_LONG).show();
                        }
                        break;
                    case QUERY_LOCATION:
                        Log.d(TAG, "querying new location");
                        //storage.updateTime();     //don't signal about update
                        break;
                }
            }

            WeatherNotificationManager.update(updater.context);
            updater.onCompleted.run();
        }
    }

}
