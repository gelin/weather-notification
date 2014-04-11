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

import static ru.gelin.android.weather.notification.skin.impl.PreferenceKeys.*;
import static ru.gelin.android.weather.notification.skin.impl.ResourceIdFactory.STRING;

/**
 *  Weather notification receiver for Yota back screen.
 */
public class SkinWeatherNotificationReceiver extends WeatherNotificationReceiver {

    /** Key to store the weather in the bundle */
    static final String WEATHER_KEY = "weather";
    private static final String SEPARATOR = " ";

    /** Handler to receive the weather */
    static Handler handler;

    /** Temperature formatter */
    protected TemperatureFormat tempFormat = createTemperatureFormat();

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

    BSNotificationManager getBSNotificationManager(Context context) {
        return new BSNotificationManager(context);
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

        ResourceIdFactory ids = ResourceIdFactory.getInstance(context);
        SharedPreferences prefs =
                PreferenceManager.getDefaultSharedPreferences(context);

        TemperatureType unit = TemperatureType.valueOf(prefs.getString(
                TEMP_UNIT, TEMP_UNIT_DEFAULT));
        TemperatureUnit mainUnit = unit.getTemperatureUnit();
//        NotificationStyle textStyle = NotificationStyle.valueOf(prefs.getString(
//                NOTIFICATION_TEXT_STYLE, NOTIFICATION_TEXT_STYLE_DEFAULT));

        BSNotification notification = new BSNotification();
        BSNotification.Builder builder = new BSNotification.Builder();

//        builder.setSmallIcon(getNotificationIconId());

        if (weather.isEmpty() || weather.getConditions().size() <= 0) {
//            notification.tickerText = context.getString(ids.id(STRING, "unknown_weather"));
        } else {
//            notification.tickerText = formatTicker(context, weather, unit);
//            notification.iconLevel = getNotificationIconLevel(weather, mainUnit);
        }

        builder.setWhen(weather.getTime().getTime());
//        notification.flags |= Notification.FLAG_NO_CLEAR;
//        notification.flags |= Notification.FLAG_ONGOING_EVENT;

//        notification.contentView = new RemoteViews(context.getPackageName(),
//                getNotificationLayoutId(context, textStyle, unit));
//        RemoteWeatherLayout layout = createRemoteWeatherLayout(
//                context, notification.contentView, unit);
//        layout.bind(weather);
        builder.setContentTitle(formatTitle(context, weather, unit));
        builder.setContentText(formatText(context, ids, weather, mainUnit));

//        notification.contentIntent = getContentIntent(context);
        //notification.contentIntent = getMainActivityPendingIntent(context);

        getBSNotificationManager(context).notify(getNotificationId(), notification);

        notifyHandler(weather);
    }

    /**
     *  Returns the notification ID for the skin.
     *  Different skins withing the same application must return different results here.
     */
    protected int getNotificationId() {
        return this.getClass().getName().hashCode();
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

    protected String formatTitle(Context context, Weather weather, TemperatureType unit) {
        ResourceIdFactory ids = ResourceIdFactory.getInstance(context);
        WeatherCondition condition = weather.getConditions().get(0);
        Temperature tempC = condition.getTemperature(TemperatureUnit.C);
        Temperature tempF = condition.getTemperature(TemperatureUnit.F);
        return context.getString(ids.id(STRING, "notification_ticker"),
                weather.getLocation().getText(),
                tempFormat.format(tempC.getCurrent(), tempF.getCurrent(), unit));
    }

    protected String formatText(Context context, ResourceIdFactory ids, Weather weather, TemperatureUnit unit) {
        StringBuilder forecastsText = new StringBuilder();
        for (int i = 1; i < 4; i++) {
            if (weather.getConditions().size() <= i) {
                break;
            }
            WeatherCondition forecast = weather.getConditions().get(i);
            Temperature temp = forecast.getTemperature(unit);
            Date day = addDays(weather.getTime(), i);
            forecastsText.append(context.getString(ids.id(ResourceIdFactory.STRING, "forecast_text"),
                    day,
                    tempFormat.format(temp.getHigh()),
                    tempFormat.format(temp.getLow())));
            forecastsText.append(SEPARATOR);
        }
        return forecastsText.toString();
    }

    Date addDays(Date date, int days) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_YEAR, days);
        return calendar.getTime();
    }

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

    /**
     *  Creates the temperature formatter.
     */
    protected TemperatureFormat createTemperatureFormat() {
        return new TemperatureFormat();
    }

}
