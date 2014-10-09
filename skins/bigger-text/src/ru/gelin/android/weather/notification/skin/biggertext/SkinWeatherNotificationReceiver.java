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

package ru.gelin.android.weather.notification.skin.biggertext;

import android.content.ComponentName;
import android.content.Context;
import ru.gelin.android.weather.TemperatureUnit;
import ru.gelin.android.weather.Weather;
import ru.gelin.android.weather.notification.skin.impl.BaseWeatherNotificationReceiver;

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
        return R.drawable.temp_icon_light;
    }

    @Override
    protected int getNotificationIconLevel(Weather weather, TemperatureUnit unit) {
        return weather.getConditions().get(0).
                getTemperature(unit).getCurrent() + ICON_LEVEL_SHIFT;
    }

    @Override
    protected WeatherFormatter getWeatherFormatter(Context context, Weather weather) {
        return new WeatherFormatter(context, weather);
    }
}