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

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.test.AndroidTestCase;

public class OpenWeatherMapApiKeyTest extends AndroidTestCase {

    public void testDefaultKey() {
        OpenWeatherMapApiKey key = new OpenWeatherMapApiKey(getContext());
//        assertEquals(OpenWeatherMapApiKey.DEFAULT_API_KEY, key.getKey());
        assertTrue(key.getKey().startsWith("840ed2"));
    }

    public void testKeyFromPreferences() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        prefs.edit().putString(OpenWeatherMapApiKey.PREFERENCE_KEY, "test_key").commit();
        OpenWeatherMapApiKey key = new OpenWeatherMapApiKey(getContext());
        assertEquals("test_key", key.getKey());
        prefs.edit().remove(OpenWeatherMapApiKey.PREFERENCE_KEY).commit();
    }

}
