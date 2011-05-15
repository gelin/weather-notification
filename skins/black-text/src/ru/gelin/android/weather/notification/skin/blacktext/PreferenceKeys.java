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

package ru.gelin.android.weather.notification.skin.blacktext;

import ru.gelin.android.weather.notification.skin.TemperatureUnit;

/**
 *  Constants for preference keys.
 */
public interface PreferenceKeys {

    /** Notification text style preference key */
    public static final String NOTIFICATION_TEXT_STYLE = "notification_text_style";
    /** Notification text style default value */
    public static final String NOTIFICATION_TEXT_STYLE_DEFAULT = NotificationStyle.BLACK_TEXT.toString();
    
    /** Temperature unit preference name */
    static final String TEMP_UNIT = "temp_unit";
    /** Temperature unit default value */
    static final String TEMP_UNIT_DEFAULT = TemperatureUnit.C.toString();

}
