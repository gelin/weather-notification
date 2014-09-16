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

import android.os.Parcel;
import android.test.InstrumentationTestCase;
import ru.gelin.android.weather.Weather;

public class ParcelableWeather2Test extends InstrumentationTestCase {

    public void testCopyConstructor() throws Exception {
        Weather weather1 = WeatherUtils.createWeather(getInstrumentation().getContext());
        Weather weather2 = new ParcelableWeather2(weather1);
        WeatherUtils.checkWeather(weather2);
    }

    public void testCopyConstructor2() throws Exception {
        Weather weather1 = WeatherUtils.createOpenWeather(getInstrumentation());
        Weather weather2 = new ParcelableWeather2(weather1);
        WeatherUtils.checkOpenWeather(weather2);
    }
    
    public void testWriteRead() throws Exception {
        Weather weather1 = WeatherUtils.createWeather(getInstrumentation().getContext());
        Parcel parcel = Parcel.obtain();
        ParcelableWeather2 weather2 = new ParcelableWeather2(weather1);
        //weather2.writeToParcel(parcel, 0);
        parcel.writeParcelable(weather2, 0);
        int position = parcel.dataPosition();
        parcel.setDataPosition(0);
        //Weather weather3 = ParcelableWeather2.CREATOR.createFromParcel(parcel);
        Weather weather3 = (Weather)parcel.readParcelable(getInstrumentation().getTargetContext().getClassLoader());
        assertEquals(position, parcel.dataPosition());  //read the same data as wrote
        WeatherUtils.checkWeather(weather3);
    }

    public void testWriteRead2() throws Exception {
        Weather weather1 = WeatherUtils.createOpenWeather(getInstrumentation());
        Parcel parcel = Parcel.obtain();
        ParcelableWeather2 weather2 = new ParcelableWeather2(weather1);
        //weather2.writeToParcel(parcel, 0);
        parcel.writeParcelable(weather2, 0);
        int position = parcel.dataPosition();
        parcel.setDataPosition(0);
        //Weather weather3 = ParcelableWeather2.CREATOR.createFromParcel(parcel);
        Weather weather3 = (Weather)parcel.readParcelable(getInstrumentation().getTargetContext().getClassLoader());
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
    
    @SuppressWarnings("deprecation")
    public void testOldVersionRead() throws Exception {
        Weather weather1 = WeatherUtils.createWeather(getInstrumentation().getContext());
        Parcel parcel = Parcel.obtain();
        ParcelableWeather weather2 = new ParcelableWeather(weather1);
        weather2.writeToParcel(parcel, 0);
        parcel.setDataPosition(0);
        Weather weather3 = ParcelableWeather2.CREATOR.createFromParcel(parcel);
        //WeatherUtils.checkWeather(weather3, WeatherUtils.Version.V_0_2);  //ideal :(
        assertTrue(weather3.isEmpty());
    }
    
    public void testParcel() {
        Parcel parcel = Parcel.obtain();
        parcel.writeString("test");
        parcel.setDataPosition(0);
        assertEquals("test", parcel.readString());
    }

}
