package ru.gelin.android.weather.notification;

import ru.gelin.android.weather.Weather;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.view.View;

public class WeatherPreference extends Preference implements OnSharedPreferenceChangeListener {

    public WeatherPreference(Context context) {
        super(context);
        setLayoutResource(R.layout.weather);
    }
    public WeatherPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        setLayoutResource(R.layout.weather);
    }
    public WeatherPreference(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setLayoutResource(R.layout.weather);
    }

    @Override
    protected void onBindView(View view) {
        super.onBindView(view);
        Context context = getContext();
        WeatherStorage storage = new WeatherStorage(context);
        WeatherLayout layout = new WeatherLayout(context, view);
        Weather weather = storage.load();
        layout.bind(weather);
    }
    
    @Override
    protected void onClick() {
        super.onClick();
        UpdateService.start(getContext(), true);
    }
    
    @Override
    protected void onAttachedToHierarchy(PreferenceManager preferenceManager) {
        super.onAttachedToHierarchy(preferenceManager);
        getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }
    
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
            String key) {
        if (!getKey().equals(key)) {
            return;
        }
        callChangeListener(sharedPreferences.getAll().get(key));
        notifyChanged();
    }

}
