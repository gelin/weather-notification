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

package ru.gelin.android.weather.notification.skin.v11;

import android.app.Notification;
import android.content.ComponentName;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import ru.gelin.android.weather.TemperatureUnit;
import ru.gelin.android.weather.Weather;
import ru.gelin.android.weather.notification.skin.impl.BaseWeatherNotificationReceiver;
import ru.gelin.android.weather.notification.skin.impl.WeatherNotificationReceiver;

/**
 *  Extends the basic notification receiver.
 *  Overwrites the weather info activity intent and getting of the icon resource.
 */
public class SkinWeatherNotificationReceiver extends WeatherNotificationReceiver {

    /** Notification ID */
    static final int ID = 1;

    @Override
    protected void notify(Context context, Weather weather) {
        Notification notification = new Notification.Builder(context).
                setContentTitle("Weather").
                setSmallIcon(R.drawable.status_icon).
                setLargeIcon(getLargeIcon(context)).
                getNotification();

        getNotificationManager(context).notify(ID, notification);
    }

    @Override
    protected void cancel(Context context) {
        getNotificationManager(context).cancel(ID);
    }

    Bitmap getLargeIcon(Context context) {
        Resources res = context.getResources();
        Bitmap bitmap = Bitmap.createBitmap(
                res.getDimensionPixelSize(android.R.dimen.notification_large_icon_width),
                res.getDimensionPixelSize(android.R.dimen.notification_large_icon_height),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        paint.setARGB(255, 255, 0, 0);
        canvas.drawCircle(bitmap.getWidth() / 2,
                bitmap.getHeight() / 2,
                bitmap.getWidth() / 3,
                paint);
        return bitmap;
    }

}