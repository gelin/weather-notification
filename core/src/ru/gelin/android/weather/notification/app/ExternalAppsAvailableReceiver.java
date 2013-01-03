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

package ru.gelin.android.weather.notification.app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import ru.gelin.android.weather.notification.AppUtils;

/**
 *  Broadcast receiver which receives event about availability of new applicatons installed on SD card.
 *  Starts UpdateService to update weather information for skins installed on SD card.
 */
public class ExternalAppsAvailableReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        AppUtils.startUpdateService(context);
    }

}
