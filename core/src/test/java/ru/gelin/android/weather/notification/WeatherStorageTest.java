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

package ru.gelin.android.weather.notification;

import android.content.Context;
import android.os.Build;
import androidx.test.core.app.ApplicationProvider;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import ru.gelin.android.weather.Weather;

import static org.junit.Assert.assertEquals;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = {Build.VERSION_CODES.P})
public class WeatherStorageTest {

    private final Context context = ApplicationProvider.getApplicationContext();

    @Test
    public void testSaveLoad3() throws Exception {
        Weather weather1 = WeatherUtils.createOpenWeather(context);
        WeatherStorage storage = new WeatherStorage(context);
        storage.save(weather1);
        Weather weather2 = storage.load();
        WeatherUtils.checkOpenWeather(weather2);
    }

    @Test
    public void testSaveLoad4() throws Exception {
        Weather weather1 = WeatherUtils.createOpenWeather(context);
        Weather weather2 = new ParcelableWeather2(weather1);
        WeatherStorage storage = new WeatherStorage(context);
        storage.save(weather2);
        Weather weather3 = storage.load();
        WeatherUtils.checkOpenWeather(weather3);
    }

// Ignoring tests for old weather version 0.2.

//    public void testBackwardCompatibility() throws Exception {
//        Weather weather1 = WeatherUtils.createWeather(getContext());
//        WeatherStorage newStorage = new WeatherStorage(getContext());
//        newStorage.save(weather1);
//
//        ru.gelin.android.weather.v_0_2.notification.WeatherStorage storage =
//            new ru.gelin.android.weather.v_0_2.notification.WeatherStorage(getContext());
//        ru.gelin.android.weather.v_0_2.Weather weather2 = storage.load();
//
//        WeatherUtils.checkWeather(weather2);
//    }

    /*  Don't want to support saving of the old weather by the new storage
    public void testBackwardCompatibility2() throws Exception {
        ru.gelin.android.weather.v_0_2.Weather weather1 = WeatherUtils.createWeather_v_0_2();
        WeatherStorage newStorage = new WeatherStorage(getContext());
        newStorage.save(WeatherUtils.convert(weather1));

        Weather weather2 = newStorage.load();

        WeatherUtils.checkWeather(weather2);
    }
    */

//    public void testBackwardCompatibility3() throws Exception {
//        Weather weather1 = WeatherUtils.createWeather(getContext());
//
//        ru.gelin.android.weather.v_0_2.notification.WeatherStorage oldStorage =
//                new ru.gelin.android.weather.v_0_2.notification.WeatherStorage(getContext());
//        oldStorage.save(WeatherUtils.convert(weather1));
//
//        ru.gelin.android.weather.v_0_2.Weather weather2 = oldStorage.load();
//
//        WeatherUtils.checkWeather(weather2);
//    }

    @Test
    public void testRemoveOldConditions() throws Exception {
        WeatherStorage storage = new WeatherStorage(context);
        Weather weather1 = WeatherUtils.createOpenWeather(context);
        storage.save(weather1);
        assertEquals(4, storage.load().getConditions().size());
        Weather weather2 = WeatherUtils.createIncompleteOpenWeather(context);
        storage.save(weather2);
        assertEquals(1, storage.load().getConditions().size());
    }

}
