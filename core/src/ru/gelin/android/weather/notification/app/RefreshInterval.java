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

package ru.gelin.android.weather.notification.app;

/**
 *  Represents the refresh interval.
 *  Handles interval value (in milliseconds).
 */
public enum RefreshInterval {
    
    REFRESH_15M(900 * 1000),
    REFRESH_30M(1800 * 1000),
    REFRESH_1H(3600 * 1000),
    REFRESH_2H(2 * 3600 * 1000),
    REFRESH_3H(3 * 3600 * 1000),
    REFRESH_4H(4 * 3600 * 1000),
    REFRESH_6H(6 * 3600 * 1000),
    REFRESH_12H(12 * 3600 * 1000),
    REFRESH_1D(24 * 3600 * 1000);
    
    long interval;
    
    RefreshInterval(long interval) {
        this.interval = interval;
    }
    
    /**
     *  Returns refresh interval value (in milliseconds).
     */
    public long getInterval() {
        return this.interval;
    }

}
