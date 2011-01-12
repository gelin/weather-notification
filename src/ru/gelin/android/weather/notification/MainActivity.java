package ru.gelin.android.weather.notification;

import static ru.gelin.android.weather.notification.PreferenceKeys.AUTO_LOCATION;
import static ru.gelin.android.weather.notification.PreferenceKeys.ENABLE_NOTIFICATION;
import static ru.gelin.android.weather.notification.PreferenceKeys.LOCATION;
import static ru.gelin.android.weather.notification.PreferenceKeys.UNIT_SYSTEM;
import static ru.gelin.android.weather.notification.WeatherStorage.WEATHER;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.Preference.OnPreferenceClickListener;
import android.view.Window;

public class MainActivity extends PreferenceActivity 
        implements OnPreferenceClickListener, OnPreferenceChangeListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);    //before super()!
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.main_preferences);
        
        Preference weatherPreference = findPreference(WEATHER);
        weatherPreference.setOnPreferenceClickListener(this);
        weatherPreference.setOnPreferenceChangeListener(this);
        Preference notificationPreference = findPreference(ENABLE_NOTIFICATION);
        notificationPreference.setOnPreferenceChangeListener(this);
        Preference autoLocationPreference = findPreference(AUTO_LOCATION);
        autoLocationPreference.setOnPreferenceChangeListener(this);
        Preference locationPreference = findPreference(LOCATION);
        locationPreference.setOnPreferenceChangeListener(this);
        Preference unitPreference = findPreference(UNIT_SYSTEM);
        unitPreference.setOnPreferenceChangeListener(this);
        
        startUpdate();
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        String key = preference.getKey();
        if (WEATHER.equals(key)) {
            setProgressBarIndeterminateVisibility(true);
            return true;
        }
        return false;
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        String key = preference.getKey();
        if (WEATHER.equals(key)) {
            //Log.d(TAG, String.valueOf(newValue));
            setProgressBarIndeterminateVisibility(false);
            return true;
        }
        if (ENABLE_NOTIFICATION.equals(key)) {
            WeatherNotification.update(this, Boolean.TRUE.equals(newValue));
            return true;
        }
        if (AUTO_LOCATION.equals(key) || LOCATION.equals(key)) {
            startUpdate();
            return true;
        }
        if (UNIT_SYSTEM.equals(key)) {
            WeatherStorage storage = new WeatherStorage(this);
            storage.updateTime();   //force redraw weather
            return true;
        }
        return true;
    }
    
    void startUpdate() {
        setProgressBarIndeterminateVisibility(true);
        UpdateService.start(this, true);
    }

}