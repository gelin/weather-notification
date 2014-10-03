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
import android.app.NotificationManager;
import android.content.ComponentName;
import android.content.Context;
import android.util.Log;
import android.widget.RemoteViews;
import ru.gelin.android.weather.TemperatureUnit;
import ru.gelin.android.weather.Weather;
import ru.gelin.android.weather.WeatherCondition;
import ru.gelin.android.weather.notification.WeatherStorage;
import ru.gelin.android.weather.notification.skin.Tag;
import ru.gelin.android.weather.notification.skin.impl.*;

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
        cancelAll(context);
    }

    void cancelAll(Context context) {
        NotificationManager manager = getNotificationManager(context);
        for (int id = INITIAL_ID; id < MAX_ID; id++) {
            manager.cancel(id);
        }
    }

    @Override
    protected void notify(Context context, Weather weather) {
        Log.d(Tag.TAG, "displaying weather: " + weather);

        WeatherStorage storage = new WeatherStorage(context);
        storage.save(weather);

        cancelAll(context);

        if (weather.isEmpty() || weather.getConditions().size() <= 0) {
            unknownWeatherNotify(context, weather);
        } else {
            String temperature = getTemperatureText(context, weather);
            mainNotify(context, weather, temperature.charAt(0));    //TODO refactor passing of multiple parameters
            for (int i = temperature.length() - 1; i >= 0; i--) {
                notify(context, weather, INITIAL_ID + i, temperature.charAt(i));
            }
        }

        notifyHandler(weather);
    }

    String getTemperatureText(Context context, Weather weather) {
        NotificationStyler styler = createStyler(context);
        TemperatureType type = styler.getTempType();
        TemperatureFormat format = createTemperatureFormat();

        WeatherCondition condition = weather.getConditions().get(0);
        switch (type) {
            case C:
            case CF:
                return format.format(condition.getTemperature(TemperatureUnit.C).getCurrent());
            case F:
            case FC:
                return format.format(condition.getTemperature(TemperatureUnit.F).getCurrent());
            default:
                return "";  //should never happen
        }
    }

    void unknownWeatherNotify(Context context, Weather weather) {
        ResourceIdFactory ids = ResourceIdFactory.getInstance(context);
        Notification notification = createFullNotification(context, weather);
        notification.tickerText = context.getString(ids.id(STRING, "unknown_weather"));
        getNotificationManager(context).notify(INITIAL_ID, notification);
    }

    void mainNotify(Context context, Weather weather, char c) {
        Notification notification = createFullNotification(context, weather);
        notification.iconLevel = getNotificationIconLevel(c);
        getNotificationManager(context).notify(INITIAL_ID, notification);
    }

    void notify(Context context, Weather weather, int id, char c) {
        Notification notification = createNotification(context, weather);
        notification.contentView = new RemoteViews(context.getPackageName(), R.layout.notification_empty);
        notification.iconLevel = getNotificationIconLevel(c);
        getNotificationManager(context).notify(id, notification);
    }

    Notification createNotification(Context context, Weather weather) {
        Notification notification = new Notification();

        notification.icon = getNotificationIconId(weather);
        notification.when = weather.getTime().getTime();
        notification.contentIntent = getContentIntent(context);

        notification.flags |= Notification.FLAG_NO_CLEAR;
        notification.flags |= Notification.FLAG_ONGOING_EVENT;

        return notification;
    }

    Notification createFullNotification(Context context, Weather weather) {
        Notification notification = createNotification(context, weather);
        NotificationStyler styler = createStyler(context);
        TemperatureType tempType = styler.getTempType();

        notification.tickerText = formatTicker(context, weather, tempType);
        notification.contentView = new RemoteViews(context.getPackageName(), styler.getLayoutId());
        RemoteWeatherLayout layout = getRemoteWeatherLayout(context, notification.contentView, styler);
        layout.bind(weather);

        //notification.contentIntent = getMainActivityPendingIntent(context);
        return notification;
    }

    @Override
    protected ComponentName getWeatherInfoActivityComponentName() {
        return new ComponentName(SkinWeatherNotificationReceiver.class.getPackage().getName(), 
                WeatherInfoActivity.class.getName());
    }

    @Override
    protected int getNotificationIconId(Weather weather) {
        return R.drawable.char_white;   //TODO other colors
    }

    @Override
    protected int getNotificationIconLevel(Weather weather, TemperatureUnit unit) {
        return 0;  //TODO refactor
    }

    protected int getNotificationIconLevel(char c) {
        return (int)c;
    }

    @Override
    protected TemperatureFormat createTemperatureFormat() {
        return new TemperatureFormat();
    }

}