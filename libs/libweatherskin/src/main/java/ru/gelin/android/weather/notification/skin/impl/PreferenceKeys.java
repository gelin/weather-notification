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
            Integer.valueOf(Build.VERSION.SDK) < 11 || Integer.valueOf(Build.VERSION.SDK) >= 21 ?   NotificationTextStyle.BLACK_TEXT.toString() :
            NotificationTextStyle.WHITE_TEXT.toString();

    /** Notification background style preference key */
    public static final String NOTIFICATION_BACK_STYLE = "notification_back_style";
    /** Notification background style default value */
    public static final String NOTIFICATION_BACK_STYLE_DEFAULT = "DEFAULT_BACK";

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
