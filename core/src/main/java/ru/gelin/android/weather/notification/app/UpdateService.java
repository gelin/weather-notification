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

package ru.gelin.android.weather.notification.app;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.IBinder;
import android.util.Log;

import static ru.gelin.android.weather.notification.AppUtils.EXTRA_FORCE;
import static ru.gelin.android.weather.notification.AppUtils.EXTRA_VERBOSE;
import static ru.gelin.android.weather.notification.app.Tag.TAG;

/**
 *  Service to update weather.
 *  Just start it. The new weather values will be wrote to SharedPreferences
 *  (use {@link ru.gelin.android.weather.notification.WeatherStorage} to extract them).
 */
public class UpdateService extends Service {

    /** Intent which starts the service */
    Intent startIntent;

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);

        boolean verbose = false;
        boolean force = false;

        synchronized(this) {
            this.startIntent = intent;
            if (intent != null) {
                verbose = intent.getBooleanExtra(EXTRA_VERBOSE, false);
                force = intent.getBooleanExtra(EXTRA_FORCE,
                        intent.hasExtra(LocationManager.KEY_LOCATION_CHANGED)); // force weather update if location update came
            }
        }

        removeLocationUpdates();

        WeatherUpdater updater = new WeatherUpdater(this);
        updater.update(verbose, force, new Runnable() {
            @Override
            public void run() {
                stopSelf();
            }
        });
    }

    /**
     *  Unsubscribes from location updates.
     */
    void removeLocationUpdates() {
        if (this.startIntent != null && this.startIntent.hasExtra(LocationManager.KEY_LOCATION_CHANGED)) {
            Log.d(TAG, "location updated");
        }
        LocationManager manager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        manager.removeUpdates(getPendingIntent(this.startIntent));
    }

    /**
     *  Returns pending intent to start the service.
     *  @param  intent to wrap into the pending intent or null
     */
    PendingIntent getPendingIntent(Intent intent) {
        Intent serviceIntent = new Intent(intent);
        return PendingIntent.getService(this, 0, serviceIntent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
