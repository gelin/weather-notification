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

package ru.gelin.android.weather.notification.skin;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Parcel;
import androidx.test.core.app.ApplicationProvider;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import ru.gelin.android.weather.Weather;
import ru.gelin.android.weather.notification.IntentParameters;
import ru.gelin.android.weather.notification.WeatherUtils;

import static org.junit.Assert.*;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = {Build.VERSION_CODES.P})
public class WeatherNotificationManagerTest {

    private final Context context = ApplicationProvider.getApplicationContext();

    @Test
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

// Ignoring tests for old weather version 0.2.

//    public void testCreateIntent() throws Exception {
//        Weather weather = WeatherUtils.createWeather(getInstrumentation());
//        Intent intent = WeatherNotificationManager.createIntent(true, weather);
//        //Parcel parcel = Parcel.obtain();
//        //parcel.writeParcelable(intent, 0);
//        //parcel.setDataPosition(0);
//        //intent = (Intent)parcel.readParcelable(getContext().getClassLoader());
//        //intent.setExtrasClassLoader(getContext().getClassLoader());
//        //TODO: Why the serialization fails?
//        System.out.println(intent);
//        System.out.println("extras: " + intent.getExtras().keySet());
//        System.out.println("enabled: " + intent.getBooleanExtra(IntentParameters.EXTRA_ENABLE_NOTIFICATION, false));
//        System.out.println("extra weather: " + intent.getParcelableExtra(IntentParameters.EXTRA_WEATHER));
//        assertEquals(IntentParameters.ACTION_WEATHER_UPDATE, intent.getAction());
//        assertTrue(intent.hasExtra(IntentParameters.EXTRA_ENABLE_NOTIFICATION));
//        assertTrue(intent.getBooleanExtra(IntentParameters.EXTRA_ENABLE_NOTIFICATION, false));
//        assertTrue(intent.hasExtra(IntentParameters.EXTRA_WEATHER));
//        WeatherUtils.checkWeather((Weather)intent.getParcelableExtra(IntentParameters.EXTRA_WEATHER),
//                WeatherUtils.Version.V_0_2);
//    }

    @Test
    public void testCreateIntent2() throws Exception {
        Weather weather = WeatherUtils.createOpenWeather(context);
        Intent intent = WeatherNotificationManager.createIntent2(true, weather);
        Parcel parcel = Parcel.obtain();
        parcel.writeParcelable(intent, 0);
        parcel.setDataPosition(0);
        intent = parcel.readParcelable(context.getClassLoader());
        intent.setExtrasClassLoader(context.getClassLoader());
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

    @Test
    public void testCreateIntent3() throws Exception {
        Weather weather = WeatherUtils.createOpenWeather(context);
        Intent intent = WeatherNotificationManager.createIntent2(true, weather);
        Parcel parcel = Parcel.obtain();
        parcel.writeParcelable(intent, 0);
        parcel.setDataPosition(0);
        intent = parcel.readParcelable(context.getClassLoader());
        intent.setExtrasClassLoader(context.getClassLoader());
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
