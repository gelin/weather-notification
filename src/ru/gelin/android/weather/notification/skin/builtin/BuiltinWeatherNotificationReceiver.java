package ru.gelin.android.weather.notification.skin.builtin;

import static ru.gelin.android.weather.notification.AbstractWeatherLayout.formatTemp;
import static ru.gelin.android.weather.notification.PreferenceKeys.NOTIFICATION_STYLE;
import static ru.gelin.android.weather.notification.PreferenceKeys.NOTIFICATION_STYLE_DEFAULT;
import static ru.gelin.android.weather.notification.PreferenceKeys.UNIT_SYSTEM;
import static ru.gelin.android.weather.notification.PreferenceKeys.UNIT_SYSTEM_DEFAULT;
import ru.gelin.android.weather.UnitSystem;
import ru.gelin.android.weather.Weather;
import ru.gelin.android.weather.notification.NotificationStyle;
import ru.gelin.android.weather.notification.R;
import ru.gelin.android.weather.notification.skin.WeatherNotificationReceiver;
import android.app.Notification;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.RemoteViews;

/**
 *  Weather notificaiton receiver built into basic application.
 */
public class BuiltinWeatherNotificationReceiver extends
        WeatherNotificationReceiver {

    /** Notification ID */
    static final int ID = 1;
    /** Icon level shift relative to temp value */
    static final int ICON_LEVEL_SHIFT = 100;
    
    @Override
    protected void cancel(Context context) {
        getNotificationManager(context).cancel(ID);
    }

    @Override
    protected void notify(Context context, Weather weather) {
        SharedPreferences prefs =
            PreferenceManager.getDefaultSharedPreferences(context);
    
        UnitSystem unit = UnitSystem.valueOf(prefs.getString(
            UNIT_SYSTEM, UNIT_SYSTEM_DEFAULT));
        NotificationStyle style = NotificationStyle.valueOf(prefs.getString(
            NOTIFICATION_STYLE, NOTIFICATION_STYLE_DEFAULT));

        Notification notification = new Notification();
        
        notification.icon = style.getIconRes();
        
        if (weather.isEmpty() || weather.getConditions().size() <= 0) {
            notification.tickerText = context.getString(R.string.unknown_weather);
        } else {
            //http://code.google.com/p/android/issues/detail?id=6560
            //adding a hundred
            notification.iconLevel = weather.getConditions().get(0).
                    getTemperature(unit).getCurrent() + ICON_LEVEL_SHIFT;
            notification.tickerText = formatTicker(context, weather, unit);
        }
        //this.iconLevel = 223;//debug

        notification.when = weather.getTime().getTime();
        notification.flags |= Notification.FLAG_NO_CLEAR;
        
        notification.contentView = new RemoteViews(context.getPackageName(), R.layout.notification);
        RemoteWeatherLayout layout = new RemoteWeatherLayout(context, notification.contentView);
        layout.bind(weather);
        
        notification.contentIntent = getMainActivityPendingIntent(context);
        
        getNotificationManager(context).notify(ID, notification);
    }
    
    String formatTicker(Context context, Weather weather, UnitSystem unit) {
        return context.getString(R.string.notification_ticker,
                weather.getLocation().getText(),
                formatTemp(weather.getConditions().get(0).getTemperature(unit).getCurrent()));
    }

}
