/*
 *  Android Weather Notification.
 *  Copyright (C) 2010  Denis Nelubin aka Gelin
 *
 *  This library is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU Lesser General Public
 *  License as published by the Free Software Foundation; either
 *  version 2.1 of the License, or (at your option) any later version.
 *  
 *  This library is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *  Lesser General Public License for more details.
 *  
 *  You should have received a copy of the GNU Lesser General Public
 *  License along with this library; if not, write to the Free Software
 *  Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 *
 *  http://gelin.ru
 *  mailto:den@gelin.ru
 */

package ru.gelin.android.weather.notification.skin.impl;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.util.Log;
import ru.gelin.android.weather.Weather;
import ru.gelin.android.weather.notification.AppUtils;
import ru.gelin.android.weather.notification.IntentParameters;
import ru.gelin.android.weather.notification.skin.Tag;

/**
 *  Broadcast receiver which receives the weather updates.
 *  <p>
 *  The receiver should:
 *  <ul>
 *  <li>display the Weather passed in the start intent extra using {@link NotificationManager}</li>
 *  <li>hide the weather notification if necessary</li>
 *  </ul>
 *  The intent, passed to the receiver, has action {@link IntentParameters#ACTION_WEATHER_UPDATE}.
 *  The Weather Notification finds the broadcast receivers which accepts this intent type 
 *  as weather notification skins.
 *  <p>
 *  The intent contains the extras:
 *  <ul>
 *  <li>{@link IntentParameters#EXTRA_WEATHER} holds updated {@link Weather}</li>
 *  <li>{@link IntentParameters#EXTRA_ENABLE_NOTIFICATION} holds boolean flag about notification state,
 *      if false the weather notification should be hidden.</li>
 *  </ul>
 *   The intent is sent to the receiver each time the weather notification 
 *   should be updated or cleared. This can happen not on weather update, 
 *   but also when the specified skin is enabled or disabled.
 */
public abstract class WeatherNotificationReceiver extends BroadcastReceiver {

    /**
     *  Verifies the intent, extracts extras, calls {@link #notify} or {@link #cancel} methods.
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(Tag.TAG, "received: " + intent);
        if (intent == null) {
            return;
        }
        if (!IntentParameters.ACTION_WEATHER_UPDATE_2.equals(intent.getAction())) {
            return;
        }
        Log.d(Tag.TAG, "extras: " + intent.getExtras().size());
        boolean enabled = intent.getBooleanExtra(IntentParameters.EXTRA_ENABLE_NOTIFICATION, true);
        Log.d(Tag.TAG, "extra weather: " + intent.getParcelableExtra(IntentParameters.EXTRA_WEATHER));
        if (enabled) {
            Parcelable weather = intent.getParcelableExtra(IntentParameters.EXTRA_WEATHER);
            if (!(weather instanceof Weather)) {
                return;
            }
            notify(context, (Weather)weather);
        } else {
            cancel(context);
        }
    }
    
    /**
     *  Is called when a new weather value is received.
     *  @param  context current context
     *  @param  weather weather value to be displayed
     */
    protected abstract void notify(Context context, Weather weather);
    
    /**
     *  Is called when a weather notification should be canceled.
     *  @param  context current context
     */
    protected abstract void cancel(Context context);
    
    /**
     *  Returns notification manager, selected from the context.
     *  @param  context current context
     */
    protected static NotificationManager getNotificationManager(Context context) {
        return (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
    }
    
    /**
     *  Returns the PendingIntent which starts the main WeatherNotification activity.
     *  You can use it as {@link Notification#contentIntent}.
     *  @param  context current context
     */
    protected static PendingIntent getMainActivityPendingIntent(Context context) {
        Intent intent = AppUtils.getMainActivityIntent();
        return PendingIntent.getActivity(context, 0, intent, 0);
    }

}
