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

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import ru.gelin.android.weather.notification.AppUtils;

/**
 *  Broadcast receiver which receives event about changes of network connectivity.
 *  Starts UpdateService if the network goes up.
 */
public class NetworkConnectivityReceiver extends BroadcastReceiver {

    @Override
    public void onReceive (Context context, Intent intent) {
        boolean noConnection = 
            intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, false);
        if (noConnection) {
            return;
        }
        NetworkInfo info = 
            (NetworkInfo)intent.getParcelableExtra(ConnectivityManager.EXTRA_NETWORK_INFO);
        if (info == null) {
            return;
        }
        if (!info.isAvailable()) {
            return;
        }
        Log.d(Tag.TAG, "network is up");
        AppUtils.startUpdateService(context);
    }

}
