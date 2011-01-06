package ru.gelin.android.weather.notification;

import static ru.gelin.android.weather.notification.Tag.TAG;
import ru.gelin.android.weather.Location;
import ru.gelin.android.weather.SimpleLocation;
import ru.gelin.android.weather.Weather;
import ru.gelin.android.weather.WeatherSource;
import ru.gelin.android.weather.google.GoogleWeatherSource;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

/**
 *  Service to update weather.
 *  Just start it. The new weather values will be wrote to SharedPreferences
 *  (use {@link WeatherStorage} to extract them).
 */
public class UpdateService extends Service {

    /** Manual location preferences key */
    static final String LOCATION = "location";
    /** Verbose extra name for the service start intent. */
    public static String EXTRA_VERBOSE = "verbose";
    
    /** Verbose flag */
    boolean verbose = false;
    
    /**
     *  Starts the service. Convenience method.
     */
    public static void start(Context context) {
        start(context, false);
    }
    
    /**
     *  Starts the service. Convenience method.
     *  If the verbose is true, the update errors will be displayed as toasts. 
     */
    public static void start(Context context, boolean verbose) {
        Intent startIntent = new Intent(context, UpdateService.class);
        startIntent.putExtra(EXTRA_VERBOSE, verbose);
        context.startService(startIntent);
    }
    
    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        
        this.verbose = intent.getBooleanExtra(EXTRA_VERBOSE, false);
        
        SharedPreferences preferences =
                PreferenceManager.getDefaultSharedPreferences(this);
        Location location = new SimpleLocation(preferences.getString(LOCATION, ""));
     
        WeatherStorage storage = new WeatherStorage(this);
        
        WeatherSource source = new GoogleWeatherSource();
        try {
            Weather weather = source.query(location);
            Log.i(TAG, "received weather: " + weather.getTime());
            if (verbose && weather.isEmpty()) {
                Toast.makeText(this, 
                        getString(R.string.weather_update_empty, location.getText()), 
                        Toast.LENGTH_LONG).show();
            }
            storage.save(weather);
        } catch (Exception e) {
            if (verbose) {
                Toast.makeText(this, 
                        getString(R.string.weather_update_failed, e.getMessage()), 
                        Toast.LENGTH_LONG).show();
            }
            Log.w(TAG, "failed to update weather", e);
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
