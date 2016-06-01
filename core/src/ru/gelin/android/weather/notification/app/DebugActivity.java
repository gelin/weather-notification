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

import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import ru.gelin.android.weather.notification.R;

/**
 *  The activity with debug preferences.
 */
public class DebugActivity extends PreferenceActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.debug_preferences);

        DebugSettings settings = new DebugSettings(this);
        Preference apiDebugPref = findPreference(PreferenceKeys.API_DEBUG);
        apiDebugPref.setSummary(getString(R.string.api_debug_summary, settings.getDebugDir()));
    }

}