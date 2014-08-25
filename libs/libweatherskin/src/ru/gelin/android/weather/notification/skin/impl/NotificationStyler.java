/*
 *  Android Weather Notification.
 *  Copyright (C) 2014  Denis Nelubin aka Gelin
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

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import static ru.gelin.android.weather.notification.skin.impl.PreferenceKeys.*;

/**
 *  The class to get layout data based on the skin preferences.
 */
public class NotificationStyler {

    private final TemperatureType tempType;
    private final NotificationStyle notifyStyle;
    private final NotificationTextStyle textStyle;
    private final boolean showIcon;
    private final boolean showForecast;

    public NotificationStyler(Context context) {

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);

        this.tempType = TemperatureType.valueOf(prefs.getString(
                TEMP_UNIT, TEMP_UNIT_DEFAULT));
        this.notifyStyle = NotificationStyle.valueOf(prefs.getString(
                NOTIFICATION_STYLE, NOTIFICATION_STYLE_DEFAULT));
        this.textStyle = NotificationTextStyle.valueOf(prefs.getString(
                NOTIFICATION_TEXT_STYLE, NOTIFICATION_TEXT_STYLE_DEFAULT));
        this.showIcon = prefs.getBoolean(
                NOTIFICATION_ICON_STYLE, NOTIFICATION_ICON_STYLE_DEFAULT);
        this.showForecast = prefs.getBoolean(
                NOTIFICATION_FORECAST_STYLE, NOTIFICATION_FORECAST_STYLE_DEFAULT);
    }

    public TemperatureType getTempType() {
        return tempType;
    }

    public NotificationStyle getNotifyStyle() {
        return notifyStyle;
    }

    public NotificationTextStyle getTextStyle() {
        return textStyle;
    }

    public boolean isShowIcon() {
        return showIcon;
    }

    public boolean isShowForecast() {
        return showForecast;
    }

}
