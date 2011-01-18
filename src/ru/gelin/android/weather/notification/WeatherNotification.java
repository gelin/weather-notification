package ru.gelin.android.weather.notification;

import static ru.gelin.android.weather.notification.AbstractWeatherLayout.formatTemp;
import static ru.gelin.android.weather.notification.PreferenceKeys.ENABLE_NOTIFICATION;
import static ru.gelin.android.weather.notification.PreferenceKeys.NOTIFICATION_STYLE;
import static ru.gelin.android.weather.notification.PreferenceKeys.NOTIFICATION_STYLE_DEFAULT;
import static ru.gelin.android.weather.notification.PreferenceKeys.UNIT_SYSTEM;
import static ru.gelin.android.weather.notification.PreferenceKeys.UNIT_SYSTEM_DEFAULT;
import ru.gelin.android.weather.UnitSystem;
import ru.gelin.android.weather.Weather;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.RemoteViews;

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
     *  Updates the notification.
     */
    public static void update(Context context) {
        NotificationManager manager = (NotificationManager)
        context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (!isEnabled(context)) {
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
        
        UnitSystem unit = UnitSystem.valueOf(prefs.getString(
                UNIT_SYSTEM, UNIT_SYSTEM_DEFAULT));
        NotificationStyle style = NotificationStyle.valueOf(prefs.getString(
                NOTIFICATION_STYLE, NOTIFICATION_STYLE_DEFAULT));
        
        WeatherStorage storage = new WeatherStorage(context);
        Weather weather = storage.load();
        
        this.icon = style.getIconRes();
        if (weather.isEmpty()) {
            this.tickerText = context.getString(R.string.unknown_weather);
        } else {
            //http://code.google.com/p/android/issues/detail?id=6560
            //adding a hundred
            this.iconLevel = weather.getConditions().get(0).
                    getTemperature(unit).getCurrent() + ICON_LEVEL_SHIFT;
            this.tickerText = formatTicker(weather, unit);
        }
        //this.iconLevel = 223;//debug

        this.when = weather.getTime().getTime();
        this.flags |= FLAG_NO_CLEAR;
        
        this.contentView = new RemoteViews(context.getPackageName(), R.layout.notification);
        RemoteWeatherLayout layout = new RemoteWeatherLayout(context, this.contentView);
        layout.bind(weather);
        
        Intent intent = new Intent(context, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        this.contentIntent = pendingIntent;
        //setLatestEventInfo(context, this.tickerText,
        //        "", pendingIntent);
    }

    String formatTicker(Weather weather, UnitSystem unit) {
        return this.context.getString(R.string.notification_ticker,
                weather.getLocation().getText(),
                formatTemp(weather.getConditions().get(0).getTemperature(unit).getCurrent()));
    }

}