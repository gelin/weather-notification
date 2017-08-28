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

package ru.gelin.android.weather.notification.skin.biggertext;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import ru.gelin.android.weather.Weather;
import ru.gelin.android.weather.WeatherCondition;
import ru.gelin.android.weather.notification.skin.impl.Drawable2Bitmap;

public class WeatherFormatter extends ru.gelin.android.weather.notification.skin.impl.WeatherFormatter {

    public WeatherFormatter(Context context, Weather weather) {
        super(context, weather);
    }

    @Override
    protected Bitmap formatLargeIcon() {
        WeatherCondition condition = getWeather().getConditions().get(0);

        Drawable drawable = getContext().getResources().getDrawable(R.drawable.temp_icon_light);
        drawable.setLevel(condition.getTemperature(getStyler().getTempType().getTemperatureUnit()).getCurrent() +
                SkinWeatherNotificationReceiver.ICON_LEVEL_SHIFT);
        return Drawable2Bitmap.convert(drawable);
    }
    
}
