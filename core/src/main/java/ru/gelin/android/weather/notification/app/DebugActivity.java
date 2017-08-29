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
import android.app.NotificationManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.support.v4.app.ActivityCompat;
import ru.gelin.android.weather.notification.R;

import static ru.gelin.android.weather.notification.app.PermissionNotifications.WRITE_EXTERNAL_STORAGE_NOTIFICATION;
import static ru.gelin.android.weather.notification.app.PermissionRequests.WRITE_EXTERNAL_STORAGE_REQUEST;
import static ru.gelin.android.weather.notification.app.PreferenceKeys.API_DEBUG;

/**
 *  The activity with debug preferences.
 */
public class DebugActivity extends PreferenceActivity implements Preference.OnPreferenceChangeListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.debug_preferences);

        DebugSettings settings = new DebugSettings(this);
        Preference apiDebugPref = findPreference(PreferenceKeys.API_DEBUG);
        apiDebugPref.setSummary(getString(R.string.api_debug_summary, settings.getDebugDir()));
        apiDebugPref.setOnPreferenceChangeListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        DebugSettings settings = new DebugSettings(this);
        checkAndRequestPermissions(settings);
    }

    public boolean onPreferenceChange(Preference preference, Object newValue) {
        String key = preference.getKey();
        if (API_DEBUG.equals(key)) {
            DebugSettings settings = new DebugSettings(this, newValue);
            checkAndRequestPermissions(settings);
            return true;
        }
        return true;
    }

    void checkAndRequestPermissions(DebugSettings settings) {
        if (settings.isPermissionGranted()) {
            return;
        }
        if (!settings.isAPIDebug()) {
            return;
        }
        ActivityCompat.requestPermissions(this,
            new String[] { Manifest.permission.WRITE_EXTERNAL_STORAGE },
            WRITE_EXTERNAL_STORAGE_REQUEST);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == WRITE_EXTERNAL_STORAGE_REQUEST) {
            for (int i = 0; i < permissions.length; i++) {
                String permission = permissions[i];
                int result = grantResults[i];
                if (Manifest.permission.WRITE_EXTERNAL_STORAGE.equals(permission)) {
                    if (result == PackageManager.PERMISSION_GRANTED) {
                        NotificationManager manager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
                        manager.cancel(WRITE_EXTERNAL_STORAGE_NOTIFICATION);
                    } else {
                        disableWhatRequiresPermissions();
                    }
                }
            }
        }
    }

    void disableWhatRequiresPermissions() {
        DebugSettings settings = new DebugSettings(this);
        settings.setAPIDebug(false);

        finish();
        startActivity(getIntent());
    }

}
