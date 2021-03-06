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

package ru.gelin.android.weather;

/**
 *  Weather condition type priorities.
 */
class WeatherConditionTypePriority {

    static final int EXTREME_PRIORITY = 6;
    static final int THUNDERSTORM_PRIORITY = 5;
    static final int RAIN_PRIORITY = 4;
    static final int SNOW_PRIORITY = 3;
    static final int DRIZZLE_PRIORITY = 2;
    static final int ATMOSPHERE_PRIORITY = 1;
    static final int CLOUDS_PRIORITY = 0;

    private WeatherConditionTypePriority() {
        //avoid instantiation
    }

}
