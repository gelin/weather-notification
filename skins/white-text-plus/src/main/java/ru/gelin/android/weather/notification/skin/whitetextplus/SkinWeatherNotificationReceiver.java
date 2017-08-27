/*
 * Copyright 2010—2016 Denis Nelubin and others.
 *
 * This file is part of Weather Notification.
 *
 * Weather Notification is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Weather Notification is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Weather Notification.  If not, see http://www.gnu.org/licenses/.
 */

package ru.gelin.android.weather.notification.skin.whitetextplus;

import android.content.ComponentName;
import android.content.Context;
import android.widget.RemoteViews;
import ru.gelin.android.weather.TemperatureUnit;
import ru.gelin.android.weather.Weather;
import ru.gelin.android.weather.notification.skin.impl.BaseWeatherNotificationReceiver;
import ru.gelin.android.weather.notification.skin.impl.NotificationStyler;

/**
 *  Extends the basic notification receiver.
 *  Overwrites the weather info activity intent and getting of the icon resource.
 */
public class SkinWeatherNotificationReceiver extends BaseWeatherNotificationReceiver {

    /** Icon level shift relative to temp value */
    static final int ICON_LEVEL_SHIFT = 100;
    
    @Override
    protected ComponentName getWeatherInfoActivityComponentName() {
        return new ComponentName(SkinWeatherNotificationReceiver.class.getPackage().getName(), 
                WeatherInfoActivity.class.getName());
    }

    @Override
    protected int getNotificationIconId(Weather weather) {
        return R.drawable.temp_icon_white;
    }

    @Override
    protected int getNotificationIconLevel(Weather weather, TemperatureUnit unit) {
        return weather.getConditions().get(0).
                getTemperature(unit).getCurrent() + ICON_LEVEL_SHIFT;
    }
    
    @Override
    protected RemoteWeatherLayout getRemoteWeatherLayout(Context context,
                                                         RemoteViews views, NotificationStyler styler) {
        return new RemoteWeatherLayout(context, views, styler);
    }

    @Override
    protected WeatherFormatter getWeatherFormatter(Context context, Weather weather) {
        return new WeatherFormatter(context, weather);
    }

}
