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

import android.preference.Preference;
import android.preference.PreferenceCategory;
import ru.gelin.android.weather.notification.skin.SkinInfo;

import java.util.List;

import static ru.gelin.android.weather.notification.app.PreferenceKeys.SKINS_CATEGORY;

/**
 *  Main activity for ICS and later Androids.
 */
public class MainActivity4 extends BaseMainActivity {

    protected void fillSkinsPreferences(List<SkinInfo> skins) {
        PreferenceCategory skinsCategory = (PreferenceCategory)findPreference(SKINS_CATEGORY);
        for (SkinInfo skin : skins) {
            Preference switchPref = skin.getSwitchPreference(this);
            switchPref.setOnPreferenceChangeListener(this);
            skinsCategory.addPreference(switchPref);
        }
    }

}