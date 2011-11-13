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

import ru.gelin.android.weather.Weather;
import android.os.Parcel;
import android.test.AndroidTestCase;

@SuppressWarnings("deprecation")
public class ParcelableWeather_v_0_2Test extends AndroidTestCase {

    public void testCopyConstructor() throws Exception {
        Weather weather1 = WeatherUtils.createWeather();
        Weather weather2 = new ParcelableWeather_v_0_2(weather1);
        WeatherUtils.checkWeather(weather2, WeatherUtils.Version.V_0_2);
    }
    
    public void testWriteRead() throws Exception {
        Weather weather1 = WeatherUtils.createWeather();
        Parcel parcel = Parcel.obtain();
        ParcelableWeather_v_0_2 weather2 = new ParcelableWeather_v_0_2(weather1);
        weather2.writeToParcel(parcel, 0);
        parcel.setDataPosition(0);
        Weather weather3 = ParcelableWeather_v_0_2.CREATOR.createFromParcel(parcel);
        WeatherUtils.checkWeather(weather3, WeatherUtils.Version.V_0_2);
    }
    
    public void testBackwardCompatibility() throws Exception {
        Weather weather1 = WeatherUtils.createWeather();
        Parcel parcel = Parcel.obtain();
        ParcelableWeather_v_0_2 weather2 = new ParcelableWeather_v_0_2(weather1);
        weather2.writeToParcel(parcel, 0);
        parcel.setDataPosition(0);
        ru.gelin.android.weather.v_0_2.Weather weather3 = 
            ru.gelin.android.weather.v_0_2.notification.ParcelableWeather.CREATOR.createFromParcel(parcel);
        WeatherUtils.checkWeather(weather3);
    }

}
