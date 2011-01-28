package ru.gelin.android.weather.notification;

import static ru.gelin.android.weather.notification.PreferenceKeys.AUTO_LOCATION;
import static ru.gelin.android.weather.notification.PreferenceKeys.ENABLE_NOTIFICATION;
import static ru.gelin.android.weather.notification.PreferenceKeys.LOCATION;
import static ru.gelin.android.weather.notification.PreferenceKeys.REFRESH_INTERVAL;
import static ru.gelin.android.weather.notification.WeatherStorage.WEATHER;
import static ru.gelin.android.weather.notification.skin.builtin.PreferenceKeys.NOTIFICATION_ICON_STYLE;
import static ru.gelin.android.weather.notification.skin.builtin.PreferenceKeys.NOTIFICATION_TEXT_STYLE;
import static ru.gelin.android.weather.notification.skin.builtin.PreferenceKeys.UNIT_SYSTEM;
import ru.gelin.android.weather.notification.skin.WeatherNotificationManager;
import android.os.Bundle;
import android.os.Handler;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.Preference.OnPreferenceClickListener;
import android.view.Window;

public class MainActivity extends PreferenceActivity 
        implements OnPreferenceClickListener, OnPreferenceChangeListener {

    /** Handler to take notification update actions */
    Handler handler = new Handler();
    
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
        Preference iconStylePreference = findPreference(NOTIFICATION_ICON_STYLE);
        iconStylePreference.setOnPreferenceChangeListener(this);
        Preference textStylePreference = findPreference(NOTIFICATION_TEXT_STYLE);
        textStylePreference.setOnPreferenceChangeListener(this);
        Preference autoLocationPreference = findPreference(AUTO_LOCATION);
        autoLocationPreference.setOnPreferenceChangeListener(this);
        Preference locationPreference = findPreference(LOCATION);
        locationPreference.setOnPreferenceChangeListener(this);
        Preference unitPreference = findPreference(UNIT_SYSTEM);
        unitPreference.setOnPreferenceChangeListener(this);
        
        startUpdate(false);
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
            setProgressBarIndeterminateVisibility(false);
            return true;
        }
        if (ENABLE_NOTIFICATION.equals(key) || REFRESH_INTERVAL.equals(key)) {
            //force reschedule service start
            startUpdate(false);
            return true;
        }
        if (NOTIFICATION_ICON_STYLE.equals(key) || NOTIFICATION_TEXT_STYLE.equals(key)) {
            updateNotification();
            return true;
        }
        if (AUTO_LOCATION.equals(key) || 
                LOCATION.equals(key)) {
            startUpdate(true);
            return true;
        }
        if (UNIT_SYSTEM.equals(key)) {
            WeatherStorage storage = new WeatherStorage(this);
            storage.updateTime();   //force redraw weather
            updateNotification();
            return true;
        }
        return true;
    }
    
    void startUpdate(boolean force) {
        setProgressBarIndeterminateVisibility(true);
        UpdateService.start(this, true, force);
    }
    
    /**
     *  Performs the deferred update of the notification,
     *  which allows to return from onPreferenceChange handler to update
     *  preference value and update the notification later.
     */
    void updateNotification() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                WeatherNotificationManager.update(MainActivity.this);
            }
        });
    }

}