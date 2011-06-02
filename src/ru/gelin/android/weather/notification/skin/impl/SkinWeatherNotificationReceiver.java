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

package ru.gelin.android.weather.notification.skin.impl;

import static ru.gelin.android.weather.notification.skin.impl.PreferenceKeys.TEMP_UNIT;
import static ru.gelin.android.weather.notification.skin.impl.PreferenceKeys.TEMP_UNIT_DEFAULT;
import static ru.gelin.android.weather.notification.skin.impl.PreferenceKeys.NOTIFICATION_TEXT_STYLE;
import static ru.gelin.android.weather.notification.skin.impl.PreferenceKeys.NOTIFICATION_TEXT_STYLE_DEFAULT;
import ru.gelin.android.weather.Temperature;
import ru.gelin.android.weather.UnitSystem;
import ru.gelin.android.weather.Weather;
import ru.gelin.android.weather.WeatherCondition;
import ru.gelin.android.weather.notification.ParcelableWeather;
import ru.gelin.android.weather.notification.skin.impl.NotificationStyle;
import ru.gelin.android.weather.notification.skin.impl.RemoteWeatherLayout;
import ru.gelin.android.weather.notification.skin.impl.ResourceIdFactory;
import ru.gelin.android.weather.notification.skin.impl.TemperatureFormatter;
import ru.gelin.android.weather.notification.skin.impl.TemperatureUnit;
import ru.gelin.android.weather.notification.skin.impl.WeatherNotificationReceiver;
import android.app.Notification;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.widget.RemoteViews;

/**
 *  Weather notification receiver built into basic application.
 */
public class SkinWeatherNotificationReceiver extends
        WeatherNotificationReceiver {

    /** Notification ID */
    static final int ID = 1;
    /** Key to store the weather in the bundle */
    static final String WEATHER_KEY = "weather";
    
    /** Handler to receive the weather */
    static Handler handler;
    /** Temperature formatter */
    static TemperatureFormatter tempFormat = new TemperatureFormatter();
    
    /**
     *  Registers the handler to receive the new weather.
     *  The handler is owned by activity which have initiated the update.
     *  The handler is used to update the weather displayed by the activity. 
     */
    static synchronized void registerWeatherHandler(Handler handler) {
        SkinWeatherNotificationReceiver.handler = handler;
    }
    
    /**
     *  Unregisters the weather update handler.
     */
    static synchronized void unregisterWeatherHandler() {
        SkinWeatherNotificationReceiver.handler = null;
    }
    
    @Override
    protected void cancel(Context context) {
        getNotificationManager(context).cancel(ID);
    }

    @Override
    protected void notify(Context context, Weather weather) {
        ResourceIdFactory ids = ResourceIdFactory.getInstance(context);
        SharedPreferences prefs =
            PreferenceManager.getDefaultSharedPreferences(context);
    
        TemperatureUnit unit = TemperatureUnit.valueOf(prefs.getString(
            TEMP_UNIT, TEMP_UNIT_DEFAULT));
        NotificationStyle textStyle = NotificationStyle.valueOf(prefs.getString(
                NOTIFICATION_TEXT_STYLE, NOTIFICATION_TEXT_STYLE_DEFAULT));

        Notification notification = new Notification();
        
        notification.icon = ids.id("drawable", "status_icon");
        
        if (weather.isEmpty() || weather.getConditions().size() <= 0) {
            notification.tickerText = context.getString(ids.id("string", "unknown_weather"));
        } else {
            notification.tickerText = formatTicker(context, weather, unit);
        }

        notification.when = weather.getTime().getTime();
        notification.flags |= Notification.FLAG_NO_CLEAR;
        notification.flags |= Notification.FLAG_ONGOING_EVENT;
        
        notification.contentView = new RemoteViews(context.getPackageName(), 
                ids.id(textStyle.getLayoutResName()));
        RemoteWeatherLayout layout = new RemoteWeatherLayout(context, notification.contentView);
        layout.bind(weather);
        
        notification.contentIntent = WeatherInfoActivity.getPendingIntent(context);
        //notification.contentIntent = getMainActivityPendingIntent(context);
        
        getNotificationManager(context).notify(ID, notification);
        
        notifyHandler(weather);
    }
    
    String formatTicker(Context context, Weather weather, TemperatureUnit unit) {
        ResourceIdFactory ids = ResourceIdFactory.getInstance(context);
        WeatherCondition condition = weather.getConditions().get(0);
        Temperature tempC = condition.getTemperature(UnitSystem.SI);
        Temperature tempF = condition.getTemperature(UnitSystem.US);
        return context.getString(ids.id("string", "notification_ticker"),
                weather.getLocation().getText(),
                tempFormat.format(tempC.getCurrent(), tempF.getCurrent(), unit));
    }
    
    void notifyHandler(Weather weather) {
        synchronized (SkinWeatherNotificationReceiver.class) {   //monitor of static methods
            if (handler == null) {
                return;
            }
            Message message = handler.obtainMessage();
            Bundle bundle = message.getData();
            bundle.putParcelable(WEATHER_KEY, new ParcelableWeather(weather));
            message.sendToTarget();
        }
    }

}
