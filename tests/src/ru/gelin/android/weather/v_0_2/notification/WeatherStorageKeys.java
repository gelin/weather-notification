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

package ru.gelin.android.weather.v_0_2.notification;

public interface WeatherStorageKeys {

    /** Preference name for location. */
    static final String LOCATION = "weather_location";
    /** Preference name for time. */
    static final String TIME = "weather_time";
    /** Preference name for unit system. */
    static final String UNIT_SYSTEM = "weather_unit_system";
    /** Preference name pattern for condition text. */
    static final String CONDITION_TEXT = "weather_%d_condition_text";
    /** Preference name pattern for current temp. */
    static final String CURRENT_TEMP = "weather_%d_current_temp";
    /** Preference name pattern for low temp. */
    static final String LOW_TEMP = "weather_%d_low_temp";
    /** Preference name pattern for high temp. */
    static final String HIGH_TEMP = "weather_%d_high_temp";
    /** Preference name pattern for humidity text. */
    static final String HUMIDITY_TEXT = "weather_%d_humidity_text";
    /** Preference name pattern for wind text. */
    static final String WIND_TEXT = "weather_%d_wind_text";

}
