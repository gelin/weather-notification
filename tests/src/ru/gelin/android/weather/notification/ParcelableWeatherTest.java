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

import android.os.Parcel;
import android.test.InstrumentationTestCase;
import ru.gelin.android.weather.Weather;

@SuppressWarnings("deprecation")
public class ParcelableWeatherTest extends InstrumentationTestCase {

    public void testCopyConstructor() throws Exception {
        Weather weather1 = WeatherUtils.createWeather(getInstrumentation().getContext());
        Weather weather2 = new ParcelableWeather(weather1);
        WeatherUtils.checkWeather(weather2, WeatherUtils.Version.V_0_2);
    }
    
    public void testWriteRead() throws Exception {
        Weather weather1 = WeatherUtils.createWeather(getInstrumentation().getContext());
        Parcel parcel = Parcel.obtain();
        ParcelableWeather weather2 = new ParcelableWeather(weather1);
        weather2.writeToParcel(parcel, 0);
        int position = parcel.dataPosition();
        parcel.setDataPosition(0);
        Weather weather3 = ParcelableWeather.CREATOR.createFromParcel(parcel);
        assertEquals(position, parcel.dataPosition());  //read the same data as write
        WeatherUtils.checkWeather(weather3, WeatherUtils.Version.V_0_2);
    }
    
    public void testBackwardCompatibility() throws Exception {
        Weather weather1 = WeatherUtils.createWeather(getInstrumentation().getContext());
        Parcel parcel = Parcel.obtain();
        ParcelableWeather weather2 = new ParcelableWeather(weather1);
        weather2.writeToParcel(parcel, 0);
        parcel.setDataPosition(0);
        ru.gelin.android.weather.v_0_2.Weather weather3 = 
            ru.gelin.android.weather.v_0_2.notification.ParcelableWeather.CREATOR.createFromParcel(parcel);
        WeatherUtils.checkWeather(weather3);
    }

}
