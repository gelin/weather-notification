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

package ru.gelin.android.weather.notification.skin;

import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceScreen;

import java.util.List;

public class SkinsActivity extends UpdateNotificationActivity 
        implements OnPreferenceChangeListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        SkinManager sm = new SkinManager(this);
        List<SkinInfo> skins = sm.getInstalledSkins();
     
        PreferenceScreen screen = getPreferenceManager().createPreferenceScreen(this);
        setPreferenceScreen(screen);
        for (SkinInfo skin : skins) {
            CheckBoxPreference checkboxPref = skin.getCheckBoxPreference(this);
            checkboxPref.setOnPreferenceChangeListener(this);
        	screen.addPreference(checkboxPref);
        	Preference configPref = skin.getConfigPreference(this);
        	if (configPref != null) {
        	    screen.addPreference(configPref);
        	    configPref.setDependency(checkboxPref.getKey());  //disabled if skin is disabled
        	}
        }
    }
    
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        updateNotification();
        return true;
    }

}