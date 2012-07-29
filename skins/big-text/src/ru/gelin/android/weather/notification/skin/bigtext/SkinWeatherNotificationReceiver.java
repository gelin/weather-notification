/*
 *  Android Weather Notification.
 *  Copyright (C) 2012  Denis Nelubin aka Gelin
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

package ru.gelin.android.weather.notification.skin.bigtext;

import android.app.Notification;
import android.content.ComponentName;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.RemoteViews;
import ru.gelin.android.weather.Temperature;
import ru.gelin.android.weather.TemperatureUnit;
import ru.gelin.android.weather.Weather;
import ru.gelin.android.weather.WeatherCondition;
import ru.gelin.android.weather.notification.WeatherStorage;
import ru.gelin.android.weather.notification.skin.Tag;
import ru.gelin.android.weather.notification.skin.impl.*;

import static ru.gelin.android.weather.notification.skin.impl.PreferenceKeys.*;
import static ru.gelin.android.weather.notification.skin.impl.ResourceIdFactory.STRING;

/**
 *  Extends the basic notification receiver.
 *  Displays multiple notifications for each temperature character.
 */
public class SkinWeatherNotificationReceiver extends BaseWeatherNotificationReceiver {

    /** Initial notification ID */
    static final int INITIAL_ID = 1;
    /** Max notification ID */
    static final int MAX_ID = INITIAL_ID + 5;

    @Override
    protected void cancel(Context context) {
        Log.d(Tag.TAG, "cancelling weather");
        cancelAll();
    }

    void cancelAll(Context context) {
        NotificationManager manager = getNotificationManager(context);
        for (int id = INITIAL_ID; i++; i < MAX_ID) {
            manager.cancel(id);
        }
    }

    @Override
    protected void notify(Context context, Weather weather) {
        Log.d(Tag.TAG, "displaying weather: " + weather);

        WeatherStorage storage = new WeatherStorage(context);
        storage.save(weather);

        cancelAll();

        if (weather.isEmpty() || weather.getConditions().size() <= 0) {
            unknownWeatherNotify();
        } else {
            String temperature = getTemperatureText(context, weather);
            mainNotify(context, temperature.charAt(0));
            for (int i = 1; i < temperature.length(); i++) {
                notify(context, INITIAL_ID + i, temperature.charAt(i));
            }
        }

        notifyHandler(weather);
    }

    void unknownWeatherNotify() {

    }

    String getTemperatureText(Context context, Weather weather) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        TemperatureType type = TemperatureType.valueOf(prefs.getString(
                TEMP_UNIT, TEMP_UNIT_DEFAULT));

        WeatherCondition condition = weather.getConditions().get(0);
        switch (type) {
            case C:
            case CF:
                return this.tempFormat.format(condition.getTemperature(TemperatureUnit.C));
            case F:
            case FC:
                return this.tempFormat.format(condition.getTemperature(TemperatureUnit.F));
        }
    }

    void mainNotify(Context context, char c) {
        TemperatureType unit = TemperatureType.valueOf(prefs.getString(
                TEMP_UNIT, TEMP_UNIT_DEFAULT));
        TemperatureUnit mainUnit = unit.getTemperatureUnit();
        NotificationStyle textStyle = NotificationStyle.valueOf(prefs.getString(
                NOTIFICATION_TEXT_STYLE, NOTIFICATION_TEXT_STYLE_DEFAULT));

        Notification notification = new Notification();

        notification.icon = getNotificationIconId();

        if (weather.isEmpty() || weather.getConditions().size() <= 0) {
            notification.tickerText = context.getString(ids.id(STRING, "unknown_weather"));
        } else {
            notification.tickerText = formatTicker(context, weather, unit);
            notification.iconLevel = getNotificationIconLevel(weather, mainUnit);
        }

        notification.when = weather.getTime().getTime();
        notification.flags |= Notification.FLAG_NO_CLEAR;
        notification.flags |= Notification.FLAG_ONGOING_EVENT;

        notification.contentView = new RemoteViews(context.getPackageName(),
                getNotificationLayoutId(context, textStyle, unit));
        RemoteWeatherLayout layout = createRemoteWeatherLayout(
                context, notification.contentView, unit);
        layout.bind(weather);

        notification.contentIntent = getContentIntent(context);
        //notification.contentIntent = getMainActivityPendingIntent(context);

        getNotificationManager(context).notify(ID, notification);
    }

    void notify(Context context, int id, char c) {

    }

    @Override
    protected ComponentName getWeatherInfoActivityComponentName() {
        return new ComponentName(SkinWeatherNotificationReceiver.class.getPackage().getName(), 
                WeatherInfoActivity.class.getName());
    }

    @Override
    protected int getNotificationIconId() {
        return R.drawable.char_white;
    }

    @Override
    protected int getNotificationIconLevel(Weather weather, TemperatureUnit unit) {
        return 0;  //TODO refactor
    }

    @Override
    protected TemperatureFormatter createTemperatureFormatter() {
        return new TemperatureFormatter();
    }

}