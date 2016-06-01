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

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.view.View;
import ru.gelin.android.weather.Weather;
import ru.gelin.android.weather.notification.AppUtils;
import ru.gelin.android.weather.notification.R;
import ru.gelin.android.weather.notification.WeatherStorage;
import ru.gelin.android.weather.notification.skin.impl.WeatherLayout;

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
        AppUtils.startUpdateService(getContext(), true, true);
    }
    
    @Override
    protected void onAttachedToHierarchy(PreferenceManager preferenceManager) {
        super.onAttachedToHierarchy(preferenceManager);
        getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }
    
    //@Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
            String key) {
        if (!getKey().equals(key)) {
            return;
        }
        callChangeListener(sharedPreferences.getAll().get(key));
        notifyChanged();
    }

}
