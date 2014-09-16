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

package ru.gelin.android.weather.notification;

import android.content.Context;
import android.test.InstrumentationTestCase;
import ru.gelin.android.weather.Weather;

public class WeatherStorageTest extends InstrumentationTestCase {

    private Context getContext() {
        return getInstrumentation().getContext();
    }

    public void testSaveLoad() throws Exception {
        Weather weather1 = WeatherUtils.createWeather(getContext());
        WeatherStorage storage = new WeatherStorage(getContext());
        storage.save(weather1);
        Weather weather2 = storage.load();
        WeatherUtils.checkWeather(weather2);
    }

    public void testSaveLoad2() throws Exception {
        Weather weather1 = WeatherUtils.createWeather(getContext());
        Weather weather2 = new ParcelableWeather2(weather1);
        WeatherStorage storage = new WeatherStorage(getContext());
        storage.save(weather2);
        Weather weather3 = storage.load();
        WeatherUtils.checkWeather(weather3);
    }

    public void testSaveLoad3() throws Exception {
        Weather weather1 = WeatherUtils.createOpenWeather(getInstrumentation());
        WeatherStorage storage = new WeatherStorage(getContext());
        storage.save(weather1);
        Weather weather2 = storage.load();
        WeatherUtils.checkOpenWeather(weather2);
    }

    public void testSaveLoad4() throws Exception {
        Weather weather1 = WeatherUtils.createOpenWeather(getInstrumentation());
        Weather weather2 = new ParcelableWeather2(weather1);
        WeatherStorage storage = new WeatherStorage(getContext());
        storage.save(weather2);
        Weather weather3 = storage.load();
        WeatherUtils.checkOpenWeather(weather3);
    }
    
    public void testBackwardCompatibility() throws Exception {
        Weather weather1 = WeatherUtils.createWeather(getContext());
        WeatherStorage newStorage = new WeatherStorage(getContext());
        newStorage.save(weather1);
        
        ru.gelin.android.weather.v_0_2.notification.WeatherStorage storage = 
            new ru.gelin.android.weather.v_0_2.notification.WeatherStorage(getContext());
        ru.gelin.android.weather.v_0_2.Weather weather2 = storage.load();
        
        WeatherUtils.checkWeather(weather2);
    }

    /*  Don't want to support saving of the old weather by the new storage
    public void testBackwardCompatibility2() throws Exception {
        ru.gelin.android.weather.v_0_2.Weather weather1 = WeatherUtils.createWeather_v_0_2();
        WeatherStorage newStorage = new WeatherStorage(getContext());
        newStorage.save(WeatherUtils.convert(weather1));

        Weather weather2 = newStorage.load();

        WeatherUtils.checkWeather(weather2);
    }
    */

    public void testBackwardCompatibility3() throws Exception {
        Weather weather1 = WeatherUtils.createWeather(getContext());

        ru.gelin.android.weather.v_0_2.notification.WeatherStorage oldStorage =
                new ru.gelin.android.weather.v_0_2.notification.WeatherStorage(getContext());
        oldStorage.save(WeatherUtils.convert(weather1));

        ru.gelin.android.weather.v_0_2.Weather weather2 = oldStorage.load();

        WeatherUtils.checkWeather(weather2);
    }

    public void testRemoveOldConditions() throws Exception {
        WeatherStorage storage = new WeatherStorage(getContext());
        Weather weather1 = WeatherUtils.createWeather(getContext());
        storage.save(weather1);
        assertEquals(4, storage.load().getConditions().size());
        Weather weather2 = WeatherUtils.createIncompleteOpenWeather(getInstrumentation());
        storage.save(weather2);
        assertEquals(1, storage.load().getConditions().size());
    }

}
