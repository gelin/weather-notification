package ru.gelin.android.weather.notification;

import static ru.gelin.android.weather.notification.Tag.TAG;
import ru.gelin.android.weather.Location;
import ru.gelin.android.weather.SimpleLocation;
import ru.gelin.android.weather.Weather;
import ru.gelin.android.weather.WeatherSource;
import ru.gelin.android.weather.google.GoogleWeatherSource;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;

/**
 *  Service to update weather.
 *  Just start it. The new weather values will be wrote to SharedPreferences
 *  (use {@link WeatherStorage} to extract them).
 */
public class UpdateService extends Service {

    /** Manual location preferences key */
    static final String LOCATION = "location";
    
    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        
        SharedPreferences preferences =
                PreferenceManager.getDefaultSharedPreferences(this);
        Location location = new SimpleLocation(preferences.getString(LOCATION, ""));
     
        WeatherStorage storage = new WeatherStorage(this);
        
        WeatherSource source = new GoogleWeatherSource();
        try {
            Weather weather = source.query(location);
            Log.i(TAG, "received weather: " + weather.getTime());
            storage.save(weather);
        } catch (Exception e) {
            Log.w(TAG, "failed to update weather", e);
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
