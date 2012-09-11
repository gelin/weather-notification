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

import static ru.gelin.android.weather.notification.skin.impl.PreferenceKeys.NOTIFICATION_TEXT_STYLE;
import static ru.gelin.android.weather.notification.skin.impl.PreferenceKeys.TEMP_UNIT;
import static ru.gelin.android.weather.notification.skin.impl.PreferenceKeys.WS_UNIT;
import static ru.gelin.android.weather.notification.skin.impl.ResourceIdFactory.XML;
import ru.gelin.android.weather.notification.skin.UpdateNotificationActivity;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;

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
        
        /*  TODO: why this doesn't work?
        PreferenceScreen screen = getPreferenceScreen();
        screen.setOnPreferenceClickListener(this);
        screen.setOnPreferenceChangeListener(this); 
        */
        
        Preference textStylePreference = findPreference(NOTIFICATION_TEXT_STYLE);
        if (textStylePreference != null) {
            textStylePreference.setOnPreferenceChangeListener(this);
        }
        Preference unitPreference = findPreference(TEMP_UNIT);
        unitPreference.setOnPreferenceChangeListener(this);
        Preference wsunitPreference = findPreference(WS_UNIT);
        wsunitPreference.setOnPreferenceChangeListener(this);
    }

    //@Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        String key = preference.getKey();
        if (NOTIFICATION_TEXT_STYLE.equals(key) ||
                TEMP_UNIT.equals(key)||
                WS_UNIT.equals(key)) {
            updateNotification();
            return true;
        }
        return false;
    }

}