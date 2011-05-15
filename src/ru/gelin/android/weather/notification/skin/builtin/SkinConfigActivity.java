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

package ru.gelin.android.weather.notification.skin.builtin;

import static ru.gelin.android.weather.notification.skin.PreferenceKeys.TEMP_UNIT;
import static ru.gelin.android.weather.notification.skin.builtin.PreferenceKeys.NOTIFICATION_TEXT_STYLE;
import ru.gelin.android.weather.notification.R;
import ru.gelin.android.weather.notification.skin.WeatherNotificationManager;
import android.os.Bundle;
import android.os.Handler;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.Preference.OnPreferenceChangeListener;

public class SkinConfigActivity extends PreferenceActivity 
        implements OnPreferenceChangeListener {

    /** Handler to take notification update actions */
    Handler handler = new Handler();
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.skin_preferences);
        
        /*  TODO: why this doesn't work?
        PreferenceScreen screen = getPreferenceScreen();
        screen.setOnPreferenceClickListener(this);
        screen.setOnPreferenceChangeListener(this); 
        */
        
        Preference textStylePreference = findPreference(NOTIFICATION_TEXT_STYLE);
        textStylePreference.setOnPreferenceChangeListener(this);
        Preference unitPreference = findPreference(TEMP_UNIT);
        unitPreference.setOnPreferenceChangeListener(this);
    }

    //@Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        String key = preference.getKey();
        if (NOTIFICATION_TEXT_STYLE.equals(key) || TEMP_UNIT.equals(key)) {
            updateNotification();
            return true;
        }
        return false;
    }
    
    /**
     *  Performs the deferred update of the notification,
     *  which allows to return from onPreferenceChange handler to update
     *  preference value and update the notification later.
     */
    void updateNotification() {
        handler.post(new Runnable() {
            //@Override
            public void run() {
                WeatherNotificationManager.update(SkinConfigActivity.this);
            }
        });
    }

}