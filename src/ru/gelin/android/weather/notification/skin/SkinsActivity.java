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

package ru.gelin.android.weather.notification.skin;

import static ru.gelin.android.weather.notification.skin.PreferenceKeys.SKIN_ENABLED_PATTERN;

import java.util.List;

import ru.gelin.android.weather.notification.UpdateNotificationActivity;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceScreen;
import android.preference.Preference.OnPreferenceChangeListener;

public class SkinsActivity extends UpdateNotificationActivity 
        implements OnPreferenceChangeListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        SkinManager sm = new SkinManager(this);
        List<SkinInfo> skins = sm.getInstalledSkins();
     
        PreferenceScreen screen = getPreferenceManager().createPreferenceScreen(this); 
        for (SkinInfo skin : skins) {
            CheckBoxPreference checkboxPref = skin.getCheckBoxPreference(this);
            checkboxPref.setOnPreferenceChangeListener(this);
        	screen.addPreference(checkboxPref);
        	Preference configPref = skin.getConfigPreference(this);
        	if (configPref != null) {
        	    screen.addPreference(configPref);
        	}
        }
        setPreferenceScreen(screen);
    }
    
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        updateNotification();
        return true;
    }

}