/*
 * Copyright 2010—2017 Denis Nelubin and others.
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

package ru.gelin.android.weather.openweathermap;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.preference.PreferenceManager;

/**
 * A class to take API key from properties, resources and compiled in.
 */
public class OpenWeatherMapApiKey {

    /** Default API key */
    static final String DEFAULT_API_KEY = "616a1aaacb2a1e3e3ca80c8e78455f76";

    /** API key preference name */
    public static final String PREFERENCE_KEY = "openweathermap_api_key";

    /** API key resource name */
    private static final String RESOURCE_NAME = "openweathermap_api_key";

    private final Context context;

    public OpenWeatherMapApiKey(Context context) {
        this.context = context;
    }

    public String getKey() {
        String key = getKeyFromPreferences();
        if (key == null) {
            key = getKeyFromResources();
        }
        if (key == null) {
            key = getDefaultKey();
        }
        return key;
    }

    private String getKeyFromPreferences() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(PREFERENCE_KEY, null);
    }

    private String getKeyFromResources() {
        Resources resources = context.getResources();
        int id = resources.getIdentifier(RESOURCE_NAME, "string", context.getPackageName());
        if (id != 0) {
            return resources.getString(id);
        } else {
            return null;
        }
    }

    private String getDefaultKey() {
        return DEFAULT_API_KEY;
    }

}
