/*
 *  Android Weather Notification.
 *  Copyright (C) 2010  Denis Nelubin aka Gelin
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

package ru.gelin.android.weather.notification.skin.impl;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceGroup;
import android.preference.PreferenceManager;
import ru.gelin.android.weather.notification.skin.UpdateNotificationActivity;

import static ru.gelin.android.weather.notification.skin.impl.ResourceIdFactory.XML;

/**
 *  Base class for skin configuration activity.
 */
public class BaseConfigActivity extends UpdateNotificationActivity 
        implements OnPreferenceChangeListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ResourceIdFactory ids = ResourceIdFactory.getInstance(this);

        addPreferencesFromResource(ids.id(XML, "skin_preferences"));
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        enablePreferences(prefs.getString(PreferenceKeys.NOTIFICATION_STYLE, PreferenceKeys.NOTIFICATION_STYLE_DEFAULT));
        
        addPreferenceListener(getPreferenceScreen());
    }

    private void addPreferenceListener(PreferenceGroup prefs) {
        for (int i = 0; i < prefs.getPreferenceCount(); i++) {
            Preference pref = prefs.getPreference(i);
            if (pref instanceof PreferenceGroup) {
                addPreferenceListener((PreferenceGroup) pref);
            } else {
                pref.setOnPreferenceChangeListener(this);
            }
        }
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        if (PreferenceKeys.NOTIFICATION_STYLE.equals(preference.getKey())) {
            enablePreferences(newValue);
        }
        updateNotification();
        return true;
    }

    private void enablePreferences(Object notificationStyleValue) {
        boolean enabled = NotificationStyle.CUSTOM_STYLE.equals(
                NotificationStyle.valueOf(String.valueOf(notificationStyleValue)));
        findPreference(PreferenceKeys.NOTIFICATION_TEXT_STYLE).setEnabled(enabled);
        findPreference(PreferenceKeys.NOTIFICATION_ICON_STYLE).setEnabled(enabled);
        findPreference(PreferenceKeys.NOTIFICATION_FORECASTS_STYLE).setEnabled(enabled);
    }

}