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

package ru.gelin.android.weather.v_0_2.notification;

public interface WeatherStorageKeys {

    /** Preference name for location. */
    String LOCATION = "weather_location";
    /** Preference name for time. */
    String TIME = "weather_time";
    /** Preference name for unit system. */
    String UNIT_SYSTEM = "weather_unit_system";
    /** Preference name pattern for condition text. */
    String CONDITION_TEXT = "weather_%d_condition_text";
    /** Preference name pattern for current temp. */
    String CURRENT_TEMP = "weather_%d_current_temp";
    /** Preference name pattern for low temp. */
    String LOW_TEMP = "weather_%d_low_temp";
    /** Preference name pattern for high temp. */
    String HIGH_TEMP = "weather_%d_high_temp";
    /** Preference name pattern for humidity text. */
    String HUMIDITY_TEXT = "weather_%d_humidity_text";
    /** Preference name pattern for wind text. */
    String WIND_TEXT = "weather_%d_wind_text";

}
