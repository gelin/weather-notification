/*
 * Copyright 2010—2017 Denis Nelubin and others.
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

package ru.gelin.android.weather.notification.app;

public class PermissionRequests {

    private PermissionRequests() {
        // avoid instantiation
    }

    public static final int NULL_REQUEST = -1;

    public static final int ACCESS_COARSE_LOCATION_REQUEST = 0;

    public static final int ACCESS_FINE_LOCATION_REQUEST = 1;

    public static final int WRITE_EXTERNAL_STORAGE_REQUEST = 2;

}
