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
import android.os.Parcel;
import androidx.test.core.app.ApplicationProvider;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import ru.gelin.android.weather.Weather;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = {Build.VERSION_CODES.P})
public class ParcelableWeather2Test {

    private final Context context = ApplicationProvider.getApplicationContext();

    @Test
    public void testCopyConstructor2() throws Exception {
        Weather weather1 = WeatherUtils.createOpenWeather(context);
        Weather weather2 = new ParcelableWeather2(weather1);
        WeatherUtils.checkOpenWeather(weather2);
    }

    @Test
    public void testWriteRead2() throws Exception {
        Weather weather1 = WeatherUtils.createOpenWeather(context);
        Parcel parcel = Parcel.obtain();
        ParcelableWeather2 weather2 = new ParcelableWeather2(weather1);
        //weather2.writeToParcel(parcel, 0);
        parcel.writeParcelable(weather2, 0);
        int position = parcel.dataPosition();
        parcel.setDataPosition(0);
        //Weather weather3 = ParcelableWeather2.CREATOR.createFromParcel(parcel);
        Weather weather3 = parcel.readParcelable(context.getClassLoader());
        assertEquals(position, parcel.dataPosition());  //read the same data as wrote
        WeatherUtils.checkOpenWeather(weather3);
    }

    /*  Not compatible, ParcelableWeather is compatible
    public void testBackwardCompatibility() throws Exception {
        Weather weather1 = WeatherUtils.createWeather();
        Parcel parcel = Parcel.obtain();
        ParcelableWeather2 weather2 = new ParcelableWeather2(weather1);
        weather2.writeToParcel(parcel, 0);
        parcel.setDataPosition(0);
        ru.gelin.android.weather.v_0_2.Weather weather3 =
            ru.gelin.android.weather.v_0_2.notification.ParcelableWeather2.CREATOR.createFromParcel(parcel);
        WeatherUtils.checkWeather(weather3);
    }
    */

    @Test
    @SuppressWarnings("deprecation")
    public void testOldVersionRead() throws Exception {
        Weather weather1 = WeatherUtils.createOpenWeather(context);
        Parcel parcel = Parcel.obtain();
        ParcelableWeather weather2 = new ParcelableWeather(weather1);
        weather2.writeToParcel(parcel, 0);
        parcel.setDataPosition(0);
        Weather weather3 = ParcelableWeather2.CREATOR.createFromParcel(parcel);
        //WeatherUtils.checkWeather(weather3, WeatherUtils.Version.V_0_2);  //ideal :(
        assertTrue(weather3.isEmpty());
    }

    @Test
    public void testParcel() {
        Parcel parcel = Parcel.obtain();
        parcel.writeString("test");
        parcel.setDataPosition(0);
        assertEquals("test", parcel.readString());
    }

}
