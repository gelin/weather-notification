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

package ru.gelin.android.weather.notification.skin.impl;

import android.os.Build;

/**
 *  Constants for preference keys.
 */
public class PreferenceKeys {

    /** Temperature unit preference name */
    static final String TEMP_UNIT = "temp_unit";
    /** Temperature unit default value */
    static final String TEMP_UNIT_DEFAULT = TemperatureType.C.toString();
    /** Wind speed unit preference name */
    static final String WS_UNIT = "ws_unit";
    static final String WS_UNIT_DEFAULT = WindUnit.MPH.toString();

    /** Notification style preference key */
    public static final String NOTIFICATION_STYLE = "notification_style";
    /** Notification style defatul value */
    public static final String NOTIFICATION_STYLE_DEFAULT = NotificationStyle.CUSTOM_STYLE.toString();

    /** Notification text style preference key */
    public static final String NOTIFICATION_TEXT_STYLE = "notification_text_style";
    /** Notification text style default value */
    public static final String NOTIFICATION_TEXT_STYLE_DEFAULT =
            Integer.valueOf(Build.VERSION.SDK) < 11 ?   NotificationTextStyle.BLACK_TEXT.toString() :
            NotificationTextStyle.WHITE_TEXT.toString();

    /** Notification icon style preference key */
    public static final String NOTIFICATION_ICON_STYLE = "notification_icon_style";
    /** Notification icon style default value */
    public static final boolean NOTIFICATION_ICON_STYLE_DEFAULT = true;

    /** Notification forecast style preference key */
    public static final String NOTIFICATION_FORECASTS_STYLE = "notification_forecasts_style";
    /** Notification forecast style default value */
    public static final boolean NOTIFICATION_FORECASTS_STYLE_DEFAULT = true;

    private PreferenceKeys() {
        //avoid instantiation
    }

}
