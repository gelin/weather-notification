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

package ru.gelin.android.weather.notification.app;

import android.app.AlertDialog;
import android.content.*;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceCategory;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Toast;
import ru.gelin.android.weather.notification.AppUtils;
import ru.gelin.android.weather.notification.R;
import ru.gelin.android.weather.notification.skin.SkinInfo;
import ru.gelin.android.weather.notification.skin.SkinManager;
import ru.gelin.android.weather.notification.skin.UpdateNotificationActivity;

import java.util.List;

import static ru.gelin.android.weather.notification.PreferenceKeys.ENABLE_NOTIFICATION;
import static ru.gelin.android.weather.notification.app.PreferenceKeys.*;

/**
 *  Main activity for old Androids.
 */
public abstract class BaseMainActivity extends UpdateNotificationActivity
        implements OnPreferenceClickListener, OnPreferenceChangeListener {

    static final Uri SKIN_SEARCH_URI=Uri.parse("market://search?q=WNS");

    static final String SKIN_PREFERENCE_PREFIX = "skin_enabled_";

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
        Preference locationTypePreference = findPreference(LOCATION_TYPE);
        locationTypePreference.setOnPreferenceChangeListener(this);
        Preference locationPreference = findPreference(LOCATION);
        locationPreference.setOnPreferenceChangeListener(this);
        
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

        fillSkinsPreferences(skins);

        if (skins.size() <= 1) {
            Toast.makeText(this, 
                    marketActivity == null ?
                            R.string.skins_install_notice_no_market :
                            R.string.skins_install_notice, 
                    Toast.LENGTH_LONG).show();
        }
        
        startUpdate(false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences prefs = getPreferenceManager().getSharedPreferences();
        boolean isManual = LocationType.LOCATION_MANUAL.toString().equals(
                prefs.getString(LOCATION_TYPE, LOCATION_TYPE_DEFAULT));
        //Toast.makeText(this, prefs.getString(LOCATION_TYPE, LOCATION_TYPE_DEFAULT), Toast.LENGTH_LONG).show();
        findPreference(LOCATION).setEnabled(isManual);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.debug_menu_item:
                Intent intent = new Intent(this, DebugActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    protected abstract void fillSkinsPreferences(List<SkinInfo> skins);

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
        if (LOCATION_TYPE.equals(key)) {
            LocationType locationType = LocationType.valueOf(String.valueOf(newValue));
            boolean isManual = LocationType.LOCATION_MANUAL.equals(locationType);
            findPreference(LOCATION).setEnabled(isManual);
            checkLocationProviderEnabled(locationType.getLocationProvider());
            startUpdate(true);
            return true;
        }
        if (LOCATION.equals(key)) {
            startUpdate(true);
            return true;
        }
        if (key.startsWith(SKIN_PREFERENCE_PREFIX)) {
            updateNotification();
            return true;
        }
        return true;
    }

    void startUpdate(boolean force) {
        setProgressBarIndeterminateVisibility(true);
        AppUtils.startUpdateService(this, true, force);
    }

    void checkLocationProviderEnabled(String locationProvider) {
        if (locationProvider == null) {
            return;
        }
        LocationManager manager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        if (!manager.isProviderEnabled(locationProvider)) {
            final Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            boolean hasSettings = intent.resolveActivity(getPackageManager()) != null;
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(R.string.location_disabled)
                   .setCancelable(true)
                   .setPositiveButton(R.string.open_settings, new DialogInterface.OnClickListener() {
                       @Override
                       public void onClick(DialogInterface dialogInterface, int i) {
                           startActivity(intent);
                       }
                   })
                   .setNegativeButton(android.R.string.cancel, null);
            builder.show();
        }
    }
    
}