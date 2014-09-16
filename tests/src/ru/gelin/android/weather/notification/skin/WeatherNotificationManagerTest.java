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

import android.content.Intent;
import android.os.Parcel;
import android.test.InstrumentationTestCase;
import ru.gelin.android.weather.Weather;
import ru.gelin.android.weather.notification.IntentParameters;
import ru.gelin.android.weather.notification.WeatherUtils;

@SuppressWarnings("deprecation")
public class WeatherNotificationManagerTest extends InstrumentationTestCase {
    
    public void testCreateIntentDisableNotification() {
        Intent intent = WeatherNotificationManager.createIntent(false, null);
        Parcel parcel = Parcel.obtain();
        intent.writeToParcel(parcel, 0);
        parcel.setDataPosition(0);
        intent.readFromParcel(parcel);
        assertTrue(intent.hasExtra(IntentParameters.EXTRA_ENABLE_NOTIFICATION));
        assertFalse(intent.getBooleanExtra(IntentParameters.EXTRA_ENABLE_NOTIFICATION, true));
        assertFalse(intent.hasExtra(IntentParameters.EXTRA_WEATHER));
    }
    
    public void testCreateIntent() throws Exception {
        Weather weather = WeatherUtils.createWeather(getInstrumentation().getContext());
        Intent intent = WeatherNotificationManager.createIntent(true, weather);
        //Parcel parcel = Parcel.obtain();
        //parcel.writeParcelable(intent, 0);
        //parcel.setDataPosition(0);
        //intent = (Intent)parcel.readParcelable(getContext().getClassLoader());
        //intent.setExtrasClassLoader(getContext().getClassLoader());
        //TODO: Why the serialization fails?
        System.out.println(intent);
        System.out.println("extras: " + intent.getExtras().keySet());
        System.out.println("enabled: " + intent.getBooleanExtra(IntentParameters.EXTRA_ENABLE_NOTIFICATION, false));
        System.out.println("extra weather: " + intent.getParcelableExtra(IntentParameters.EXTRA_WEATHER));
        assertEquals(IntentParameters.ACTION_WEATHER_UPDATE, intent.getAction());
        assertTrue(intent.hasExtra(IntentParameters.EXTRA_ENABLE_NOTIFICATION));
        assertTrue(intent.getBooleanExtra(IntentParameters.EXTRA_ENABLE_NOTIFICATION, false));
        assertTrue(intent.hasExtra(IntentParameters.EXTRA_WEATHER));
        WeatherUtils.checkWeather((Weather)intent.getParcelableExtra(IntentParameters.EXTRA_WEATHER),
                WeatherUtils.Version.V_0_2);
    }

    public void testCreateIntent2() throws Exception {
        Weather weather = WeatherUtils.createWeather(getInstrumentation().getContext());
        Intent intent = WeatherNotificationManager.createIntent2(true, weather);
        Parcel parcel = Parcel.obtain();
        parcel.writeParcelable(intent, 0);
        parcel.setDataPosition(0);
        intent = (Intent)parcel.readParcelable(getInstrumentation().getTargetContext().getClassLoader());
        intent.setExtrasClassLoader(getInstrumentation().getTargetContext().getClassLoader());
        System.out.println(intent);
        System.out.println("extras: " + intent.getExtras().keySet());
        System.out.println("enabled: " + intent.getBooleanExtra(IntentParameters.EXTRA_ENABLE_NOTIFICATION, false));
        System.out.println("extra weather: " + intent.getParcelableExtra(IntentParameters.EXTRA_WEATHER));
        assertEquals(IntentParameters.ACTION_WEATHER_UPDATE_2, intent.getAction());
        assertTrue(intent.hasExtra(IntentParameters.EXTRA_ENABLE_NOTIFICATION));
        assertTrue(intent.getBooleanExtra(IntentParameters.EXTRA_ENABLE_NOTIFICATION, false));
        assertTrue(intent.hasExtra(IntentParameters.EXTRA_WEATHER));
        WeatherUtils.checkWeather((Weather)intent.getParcelableExtra(IntentParameters.EXTRA_WEATHER),
                WeatherUtils.Version.V_0_3);
    }

    public void testCreateIntent3() throws Exception {
        Weather weather = WeatherUtils.createOpenWeather(getInstrumentation());
        Intent intent = WeatherNotificationManager.createIntent2(true, weather);
        Parcel parcel = Parcel.obtain();
        parcel.writeParcelable(intent, 0);
        parcel.setDataPosition(0);
        intent = (Intent)parcel.readParcelable(getInstrumentation().getTargetContext().getClassLoader());
        intent.setExtrasClassLoader(getInstrumentation().getTargetContext().getClassLoader());
        System.out.println(intent);
        System.out.println("extras: " + intent.getExtras().keySet());
        System.out.println("enabled: " + intent.getBooleanExtra(IntentParameters.EXTRA_ENABLE_NOTIFICATION, false));
        System.out.println("extra weather: " + intent.getParcelableExtra(IntentParameters.EXTRA_WEATHER));
        assertEquals(IntentParameters.ACTION_WEATHER_UPDATE_2, intent.getAction());
        assertTrue(intent.hasExtra(IntentParameters.EXTRA_ENABLE_NOTIFICATION));
        assertTrue(intent.getBooleanExtra(IntentParameters.EXTRA_ENABLE_NOTIFICATION, false));
        assertTrue(intent.hasExtra(IntentParameters.EXTRA_WEATHER));
        WeatherUtils.checkOpenWeather((Weather)intent.getParcelableExtra(IntentParameters.EXTRA_WEATHER));
    }

}