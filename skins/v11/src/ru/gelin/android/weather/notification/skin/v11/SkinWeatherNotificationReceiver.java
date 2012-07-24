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