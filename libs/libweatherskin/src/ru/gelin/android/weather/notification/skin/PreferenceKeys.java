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

package ru.gelin.android.weather.notification.skin;

/**
 *  Constants for preference keys.
 */
class PreferenceKeys {
    
    private PreferenceKeys() {
        //avoid instantiation
    }

    /** Pattern of the preference key for enabled skin flags */
    static final String SKIN_ENABLED_PATTERN = "skin_enabled_%s";
    /** Pattern of the preference key for preference to start skin config activity */
    static final String SKIN_CONFIG_PATTERN = "skin_config_%s";

}
