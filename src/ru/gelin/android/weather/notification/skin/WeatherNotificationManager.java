package ru.gelin.android.weather.notification.skin;

import static ru.gelin.android.weather.notification.PreferenceKeys.ENABLE_NOTIFICATION;
import static ru.gelin.android.weather.notification.PreferenceKeys.ENABLE_NOTIFICATION_DEFAULT;
import static ru.gelin.android.weather.notification.skin.WeatherNotificationReceiver.ACTION_WEATHER_UPDATE;
import static ru.gelin.android.weather.notification.skin.WeatherNotificationReceiver.EXTRA_ENABLE_NOTIFICATION;
import static ru.gelin.android.weather.notification.skin.WeatherNotificationReceiver.EXTRA_WEATHER;
import ru.gelin.android.weather.Weather;
import ru.gelin.android.weather.notification.ParcelableWeather;
import ru.gelin.android.weather.notification.WeatherStorage;
import ru.gelin.android.weather.notification.skin.builtin.BuiltinWeatherNotificationReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 *  Managers the broadcast receivers to receive the weather notification.
 */
public class WeatherNotificationManager {
    
    /**
     *  Updates the notification.
     */
    public static void update(Context context) {
        if (!isEnabled(context)) {
            cancel(context);
            return;
        }
        notify(context);
    }
    
    /**
     *  Returns true if the notification is enabled.
     */
    static boolean isEnabled(Context context) {
        SharedPreferences prefs =
            PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getBoolean(ENABLE_NOTIFICATION, ENABLE_NOTIFICATION_DEFAULT);
    }
    
    /**
     *  Cancels the notification
     */
    static void cancel(Context context) {
        Intent intent = new Intent(ACTION_WEATHER_UPDATE);  //cancel sends to all
        intent.putExtra(EXTRA_ENABLE_NOTIFICATION, false);
        context.sendBroadcast(intent);
    }
    
    /**
     *  Notifies with new weather value.
     */
    static void notify(Context context) {
        //TODO: find necessary receiver
        Intent intent = new Intent(context, BuiltinWeatherNotificationReceiver.class);
        intent.setAction(ACTION_WEATHER_UPDATE);
        intent.putExtra(EXTRA_ENABLE_NOTIFICATION, true);
        
        WeatherStorage storage = new WeatherStorage(context);
        Weather weather = storage.load();
        intent.putExtra(EXTRA_WEATHER, new ParcelableWeather(weather));
        
        context.sendBroadcast(intent);
    }

}
