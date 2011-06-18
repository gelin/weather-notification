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

package ru.gelin.android.weather.notification;

import static ru.gelin.android.weather.notification.PreferenceKeys.AUTO_LOCATION;
import static ru.gelin.android.weather.notification.PreferenceKeys.ENABLE_NOTIFICATION;
import static ru.gelin.android.weather.notification.PreferenceKeys.LOCATION;
import static ru.gelin.android.weather.notification.PreferenceKeys.REFRESH_INTERVAL;
import static ru.gelin.android.weather.notification.PreferenceKeys.SKINS;
import static ru.gelin.android.weather.notification.PreferenceKeys.SKINS_CATEGORY;
import static ru.gelin.android.weather.notification.PreferenceKeys.SKINS_INSTALL;
import static ru.gelin.android.weather.notification.WeatherStorage.WEATHER;

import java.util.List;

import ru.gelin.android.weather.notification.skin.SkinInfo;
import ru.gelin.android.weather.notification.skin.SkinManager;
import ru.gelin.android.weather.notification.skin.SkinsActivity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceCategory;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.Preference.OnPreferenceClickListener;
import android.view.Window;
import android.widget.Toast;

public class MainActivity extends UpdateNotificationActivity 
        implements OnPreferenceClickListener, OnPreferenceChangeListener {

    static final Uri SKIN_SEARCH_URI=Uri.parse("market://search?q=Weather Notification Skin");
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);    //before super()!
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.main_preferences);
        
        /*  TODO: why this doesn't work?
        PreferenceScreen screen = getPreferenceScreen();
        screen.setOnPreferenceClickListener(this);
        screen.setOnPreferenceChangeListener(this); 
        */
        
        Preference weatherPreference = findPreference(WEATHER);
        weatherPreference.setOnPreferenceClickListener(this);
        weatherPreference.setOnPreferenceChangeListener(this);
        Preference notificationPreference = findPreference(ENABLE_NOTIFICATION);
        notificationPreference.setOnPreferenceChangeListener(this);
        Preference refreshInterval = findPreference(REFRESH_INTERVAL);
        refreshInterval.setOnPreferenceChangeListener(this);
        Preference autoLocationPreference = findPreference(AUTO_LOCATION);
        autoLocationPreference.setOnPreferenceChangeListener(this);
        Preference locationPreference = findPreference(LOCATION);
        locationPreference.setOnPreferenceChangeListener(this);
        
        Preference skinsPreference = findPreference(SKINS);
        skinsPreference.setIntent(new Intent(MainActivity.this, SkinsActivity.class));
        
        Preference skinsInstallPreference = findPreference(SKINS_INSTALL);
        Intent skinsInstallIntent = new Intent(Intent.ACTION_VIEW, SKIN_SEARCH_URI);
        skinsInstallPreference.setIntent(skinsInstallIntent);
        ComponentName marketActivity = skinsInstallIntent.resolveActivity(getPackageManager());
        if (marketActivity == null) {
            PreferenceCategory skinsCategory = (PreferenceCategory)findPreference(SKINS_CATEGORY);
            skinsCategory.removePreference(skinsInstallPreference);
        }
        
        SkinManager sm = new SkinManager(this);
        List<SkinInfo> skins = sm.getInstalledSkins();
        if (skins.size() <= 1) {
            Toast.makeText(this, 
                    marketActivity == null ?
                            R.string.skins_install_notice_no_market :
                            R.string.skins_install_notice, 
                    Toast.LENGTH_LONG).show();
        }
        
        startUpdate(false);
    }
    
    //@Override
    public boolean onPreferenceClick(Preference preference) {
        String key = preference.getKey();
        if (WEATHER.equals(key)) {
            setProgressBarIndeterminateVisibility(true);
            return true;
        }
        return false;
    }

    //@Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        String key = preference.getKey();
        if (WEATHER.equals(key)) {
            setProgressBarIndeterminateVisibility(false);
            return true;
        }
        if (ENABLE_NOTIFICATION.equals(key) || REFRESH_INTERVAL.equals(key)) {
            //force reschedule service start
            startUpdate(false);
            return true;
        }
        if (AUTO_LOCATION.equals(key) || 
                LOCATION.equals(key)) {
            startUpdate(true);
            return true;
        }
        return true;
    }
    
    void startUpdate(boolean force) {
        setProgressBarIndeterminateVisibility(true);
        UpdateService.start(this, true, force);
    }
    
    /**
     *  Starts the main activity.
     */
    public static void start(Context context) {
        Intent startIntent = new Intent();
        startIntent.setClassName(Tag.class.getPackage().getName(), MainActivity.class.getName());
        startIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        context.startActivity(startIntent);
    }

}