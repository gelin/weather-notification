package ru.gelin.android.weather.notification;

import static ru.gelin.android.weather.notification.PreferenceKeys.AUTO_LOCATION;
import static ru.gelin.android.weather.notification.PreferenceKeys.AUTO_LOCATION_DEFAULT;
import static ru.gelin.android.weather.notification.PreferenceKeys.ENABLE_NOTIFICATION;
import static ru.gelin.android.weather.notification.PreferenceKeys.ENABLE_NOTIFICATION_DEFAULT;
import static ru.gelin.android.weather.notification.PreferenceKeys.LOCATION;
import static ru.gelin.android.weather.notification.PreferenceKeys.LOCATION_DEFAULT;
import static ru.gelin.android.weather.notification.PreferenceKeys.REFRESH_INTERVAL;
import static ru.gelin.android.weather.notification.PreferenceKeys.REFRESH_INTERVAL_DEFAULT;
import static ru.gelin.android.weather.notification.Tag.TAG;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import ru.gelin.android.weather.Location;
import ru.gelin.android.weather.SimpleLocation;
import ru.gelin.android.weather.Weather;
import ru.gelin.android.weather.WeatherSource;
import ru.gelin.android.weather.google.AndroidGoogleLocation;
import ru.gelin.android.weather.google.GoogleWeatherSource;
import ru.gelin.android.weather.notification.skin.WeatherNotificationManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

/**
 *  Service to update weather.
 *  Just start it. The new weather values will be wrote to SharedPreferences
 *  (use {@link WeatherStorage} to extract them).
 */
public class UpdateService extends Service implements Runnable {

    /** Verbose extra name for the service start intent. */
    public static String EXTRA_VERBOSE = "verbose";
    /** Force extra name for the service start intent. */
    public static String EXTRA_FORCE = "force";
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
    
    /**
     *  Starts the service.
     */
    public static void start(Context context) {
        start(context, false, false);
    }
    
    /**
     *  Starts the service.
     */
    public static void start(Context context, boolean verbose) {
        start(context, verbose, false);
    }
    
    /**
     *  Starts the service.
     *  If the verbose is true, the update errors will be displayed as toasts.
     *  If the force is true, the update will start even when the weather is
     *  not expired. 
     */
    public static void start(Context context, boolean verbose, boolean force) {
        Intent startIntent = new Intent(context, UpdateService.class);
        startIntent.putExtra(EXTRA_VERBOSE, verbose);
        startIntent.putExtra(EXTRA_FORCE, force);
        context.startService(startIntent);
    }
    
    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        
        synchronized(this) {
            this.startIntent = intent;
            this.verbose = intent.getBooleanExtra(EXTRA_VERBOSE, false);
            this.force = intent.getBooleanExtra(EXTRA_FORCE, false);
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
        
        boolean autoLocation = preferences.getBoolean(AUTO_LOCATION, AUTO_LOCATION_DEFAULT);
        Location location = null;
        if (autoLocation) {
            location = queryLocation();
            if (location == null) {
                internalHandler.sendEmptyMessage(QUERY_LOCATION);
                return;
            }
        } else {
            location = new SimpleLocation(preferences.getString(LOCATION, LOCATION_DEFAULT));
        }
        synchronized(this) {
            this.location = location;
        }
        
        if (location == null || location.isEmpty()) {
            internalHandler.sendEmptyMessage(UNKNOWN_LOCATION);
            return;
        }
 
        WeatherSource source = new GoogleWeatherSource();
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
                    storage.save(weather);
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
    
    /**
     *  Queries current location using android services.
     */
    Location queryLocation() {
        LocationManager manager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        android.location.Location androidLocation = 
                manager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        //Log.d(TAG, "location time: " + new Date(androidLocation.getTime()));
        //if (!this.startIntent.hasExtra(LocationManager.KEY_LOCATION_CHANGED)) {
        if (androidLocation == null || isExpired(androidLocation.getTime())) {
            manager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 
                    0, 0, getPendingIntent(this.startIntent));  //try to update immediately 
            return null;
        }
        
        Geocoder coder = new Geocoder(this);
        List<Address> addresses = null;
        try {
            addresses = coder.getFromLocation(androidLocation.getLatitude(),
                    androidLocation.getLongitude(), 1);
        } catch (IOException e) {
            Log.w(TAG, "cannot decode location", e);
        }
        if (addresses == null || addresses.size() == 0) {
            return new AndroidGoogleLocation(androidLocation);
        }
        
        return new AndroidGoogleLocation(androidLocation, addresses.get(0));
    }
    
    /**
     *  Unsubscribes from location updates.
     */
    void removeLocationUpdates() {
        if (this.startIntent.hasExtra(LocationManager.KEY_LOCATION_CHANGED)) {
            //android.location.Location location = (android.location.Location)this.startIntent.getParcelableExtra(LocationManager.KEY_LOCATION_CHANGED);
            //Log.d(TAG, "location updated: " + new Date(location.getTime()));
            Log.d(TAG, "location updated");
            LocationManager manager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
            manager.removeUpdates(getPendingIntent(this.startIntent));
        }
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
