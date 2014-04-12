/*
 *  Android Weather Notification.
 *  Copyright (C) 2011  Denis Nelubin aka Gelin
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

package ru.gelin.android.weather.notification.skin.yotaphone;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;
import com.yotadevices.sdk.notifications.BSNotification;
import com.yotadevices.sdk.notifications.BSNotificationManager;
import ru.gelin.android.weather.Temperature;
import ru.gelin.android.weather.TemperatureUnit;
import ru.gelin.android.weather.Weather;
import ru.gelin.android.weather.WeatherCondition;
import ru.gelin.android.weather.notification.ParcelableWeather2;
import ru.gelin.android.weather.notification.WeatherStorage;
import ru.gelin.android.weather.notification.skin.Tag;
import ru.gelin.android.weather.notification.skin.impl.*;

import java.util.Calendar;
import java.util.Date;


/**
 *  Weather notification receiver for Yota back screen.
 */
public class SkinWeatherNotificationReceiver extends WeatherNotificationReceiver {

    /** Key to store the weather in the bundle */
    static final String WEATHER_KEY = "weather";

    /** Handler to receive the weather */
    static Handler handler;

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
        Log.d(Tag.TAG, "cancelling weather");
//        getBSNotificationManager(context).cancel(getNotificationId());
    }

    @Override
    protected void notify(Context context, Weather weather) {
        Log.d(Tag.TAG, "displaying weather: " + weather);

        WeatherStorage storage = new WeatherStorage(context);
        storage.save(weather);

        Intent intent = new Intent(context, ShowNotificationActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);

        notifyHandler(weather);
    }

//    /**
//     *  Returns the pending intent called on click on notification.
//     *  This intent starts the weather info activity.
//     */
//    protected PendingIntent getContentIntent(Context context) {
//        Intent intent = new Intent();
//        intent.setComponent(getWeatherInfoActivityComponentName());
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        intent.addFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
//        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
//        return PendingIntent.getActivity(context, 0, intent, 0);
//    }

    protected void notifyHandler(Weather weather) {
        synchronized (BaseWeatherNotificationReceiver.class) {   //monitor of static methods
            if (handler == null) {
                return;
            }
            Message message = handler.obtainMessage();
            Bundle bundle = message.getData();
            bundle.putParcelable(WEATHER_KEY, new ParcelableWeather2(weather));
            message.sendToTarget();
        }
    }

//    /**
//     *  Returns the component name of the weather info activity
//     */
//    abstract protected ComponentName getWeatherInfoActivityComponentName();

//    /**
//     *  Returns the ID of the notification icon.
//     */
//    protected int getNotificationIconId() {
//        return return R.drawable.temp_icon_white;
//    }

//    /**
//     *  Returns the notification icon level.
//     */
//    abstract protected int getNotificationIconLevel(Weather weather, ru.gelin.android.weather.TemperatureUnit unit);

}
