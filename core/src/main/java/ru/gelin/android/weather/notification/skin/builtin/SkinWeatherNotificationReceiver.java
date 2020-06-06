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

package ru.gelin.android.weather.notification.skin.builtin;

import android.content.ComponentName;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import ru.gelin.android.weather.TemperatureUnit;
import ru.gelin.android.weather.Weather;
import ru.gelin.android.weather.notification.R;
import ru.gelin.android.weather.notification.skin.impl.BaseWeatherNotificationReceiver;

import static ru.gelin.android.weather.notification.Tag.TAG;


/**
 *  Extends the basic notification receiver.
 *  Displays notification icon with temperature or weather condition.
 */
public class SkinWeatherNotificationReceiver extends BaseWeatherNotificationReceiver {

    /** Icon level shift relative to temp value */
    static final int TEMP_ICON_LEVEL_SHIFT = 100;

    private StatusBarIconStyle statusBarIconStyle = StatusBarIconStyle.valueOf(PreferenceKeys.STATUS_BAR_ICON_STYLE_DEFAULT);

    @Override
    protected ComponentName getWeatherInfoActivityComponentName() {
        return new ComponentName(TAG, WeatherInfoActivity.class.getName());
    }

    @Override
    protected void notify(Context context, Weather weather) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        statusBarIconStyle = StatusBarIconStyle.valueOf(prefs.getString(PreferenceKeys.STATUS_BAR_ICON_STYLE, PreferenceKeys.STATUS_BAR_ICON_STYLE_DEFAULT));
        super.notify(context, weather);
    }

    @Override
    protected int getNotificationIconId(Weather weather) {
        switch (statusBarIconStyle) {
            case STATUS_BAR_WEATHER_CONDITION:
                return super.getNotificationIconId(weather);
            case STATUS_BAR_TEMPERATURE:
            default:
                return R.drawable.temp_icon;
        }
    }

    @Override
    protected int getNotificationIconLevel(Weather weather, TemperatureUnit unit) {
        switch (statusBarIconStyle) {
            case STATUS_BAR_WEATHER_CONDITION:
                return super.getNotificationIconLevel(weather, unit);
            case STATUS_BAR_TEMPERATURE:
            default:
                return weather.getConditions().get(0).getTemperature(unit).getCurrent() + TEMP_ICON_LEVEL_SHIFT;
        }
    }

}
