package ru.gelin.android.weather.notification;

import static ru.gelin.android.weather.notification.PreferenceKeys.ENABLE_NOTIFICATION;
import static ru.gelin.android.weather.notification.PreferenceKeys.UNIT_SYSTEM;
import static ru.gelin.android.weather.notification.WeatherLayout.formatTemp;
import ru.gelin.android.weather.UnitSystem;
import ru.gelin.android.weather.Weather;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 *  Represents the weather notification.
 */
public class WeatherNotification extends Notification {

    /** Notification ID */
    static final int ID = 1;
    /** Temperature image prefix */
    static final String RES_PREFIX="temp";
    /** Icon level shift relative to temp value */
    static final int ICON_LEVEL_SHIFT = 100;
    
    /** Current context */
    Context context;

    /**
     *  Updates the notification using the notification enable
     *  flag saved in preferences.
     */
    public static void update(Context context) {
        update(context, isEnabled(context));
    }
    
    /**
     *  Updates the notification using the specified notification enable flag.
     */
    public static void update(Context context, boolean enable) {
        NotificationManager manager = (NotificationManager)
        context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (!enable) {
            manager.cancel(ID);
            return;
        }
        manager.notify(ID, new WeatherNotification(context));
    }

    /**
     *  Returns true if the notification is enabled.
     */
    public static boolean isEnabled(Context context) {
        SharedPreferences prefs =
            PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getBoolean(ENABLE_NOTIFICATION, true);
    }

    WeatherNotification(Context context) {
        this.context = context;
        SharedPreferences prefs =
                PreferenceManager.getDefaultSharedPreferences(context);
        
        UnitSystem unit = UnitSystem.valueOf(prefs.getString(UNIT_SYSTEM, "SI"));
        
        WeatherStorage storage = new WeatherStorage(context);
        Weather weather = storage.load();
        
        if (weather.isEmpty()) {
            this.icon = R.drawable.icon;
            this.tickerText = context.getString(R.string.unknown_weather);
        } else {
            this.icon = R.drawable.temp_icon;
            //http://code.google.com/p/android/issues/detail?id=6560
            //adding a hundred
            this.iconLevel = weather.getConditions().get(0).
                    getTemperature(unit).getCurrent() + ICON_LEVEL_SHIFT;
            this.tickerText = formatTicker(weather, unit);
        }

        this.when = weather.getTime().getTime();
        this.flags |= FLAG_NO_CLEAR;
        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        setLatestEventInfo(context, this.tickerText,
                "", pendingIntent);
    }

    /*
    static int getIconResource(Context context, Resources res, float temperature) {
        int temp = (int)temperature;
        if (temp < -50) {
            temp = -50;
        }
        if (temp > 50) {
            temp = 50;
        }
        StringBuilder resName = new StringBuilder();

        SharedPreferences prefs =
            PreferenceManager.getDefaultSharedPreferences(context);
        if (HTC.equals(prefs.getString(STYLE, ""))) {
            resName.append("htc_");
        }

        resName.append(RES_PREFIX);

        if (temp < 0) {
            resName.append("_minus");
        } else if (temp > 0) {

            resName.append("_plus");
        }
        resName.append("_").append(String.valueOf(Math.abs(temp)));
        Log.d(TAG, "notification image: " + resName);
        return res.getIdentifier(resName.toString(),
                "drawable", R.class.getPackage().getName());
    }
    */

    String formatTicker(Weather weather, UnitSystem unit) {
        return this.context.getString(R.string.notification_ticker,
                weather.getLocation().getText(),
                formatTemp(weather.getConditions().get(0).getTemperature(unit).getCurrent()));
    }

}