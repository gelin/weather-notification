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

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;

import java.io.File;

/**
 *  A class to check global debug settings.
 */
public class DebugSettings {

    private final Context context;
    private final Object newValue;

    public DebugSettings(Context context) {
        this(context, null);
    }

    public DebugSettings(Context context, Object newValue) {
        this.context = context;
        this.newValue = newValue;
    }

    /**
     *  Returns the directory on the filesystem to store debug data into it.
     *  It's the application's directory on the SD card.
     *  May return null if the external storage is not available.
     */
    public File getDebugDir() {
        return this.context.getExternalFilesDir("debug");
    }

    /**
     * Returns true if the debug for API is enabled.
     */
    public boolean isAPIDebug() {
        if (newValue instanceof Boolean) {
            return (Boolean)newValue;
        }
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this.context);
        return prefs.getBoolean(PreferenceKeys.API_DEBUG, PreferenceKeys.API_DEBUG_DEFAULT);
    }

    /**
     * Sets the API debug flag.
     */
    public void setAPIDebug(boolean enable) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this.context);
        prefs.edit().putBoolean(PreferenceKeys.API_DEBUG, enable).commit();
    }

    /**
     * Returns true if it's granted to write debug file.
     */
    public boolean isPermissionGranted() {
        int permissionCheck = ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        return permissionCheck == PackageManager.PERMISSION_GRANTED;
    }

}
