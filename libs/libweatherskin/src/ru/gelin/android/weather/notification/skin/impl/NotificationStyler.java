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

import java.util.HashMap;
import java.util.Map;

import static ru.gelin.android.weather.notification.skin.impl.PreferenceKeys.*;

/**
 *  The class to get layout data based on the skin preferences.
 */
public class NotificationStyler {

    private static class LayoutKey {

        private final boolean showIcon;
        private final boolean showForecasts;
        private final boolean showUpdateTime;

        public LayoutKey(boolean showIcon, boolean showForecasts, boolean showUpdateTime) {
            this.showIcon = showIcon;
            this.showForecasts = showForecasts;
            this.showUpdateTime = showUpdateTime;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            LayoutKey layoutKey = (LayoutKey) o;

            if (showForecasts != layoutKey.showForecasts) return false;
            if (showIcon != layoutKey.showIcon) return false;
            if (showUpdateTime != layoutKey.showUpdateTime) return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = (showIcon ? 1 : 0);
            result = 31 * result + (showForecasts ? 1 : 0);
            result = 31 * result + (showUpdateTime ? 1 : 0);
            return result;
        }
    }

    public enum Layout {
        CUSTOM_ICON_FORECASTS("notification_icon_forecasts"),
        CUSTOM_ICON_FORECASTS_UPDATE("notification_icon_forecasts_update"),
        CUSTOM_ICON("notification_icon"),
        CUSTOM_ICON_UPDATE("notification_icon_update"),
        CUSTOM_FORECASTS("notification_forecasts"),
        CUSTOM_FORECASTS_UPDATE("notification_forecasts_update"),
        CUSTOM("notification"),
        CUSTOM_UPDATE("notification_update");

        public final String name;

        private Layout(String name) {
            this.name = name;
        }

        private static final Map<LayoutKey, Layout> MAP = new HashMap<LayoutKey, Layout>();

        static {
            MAP.put(new LayoutKey(false, false, false), Layout.CUSTOM);
            MAP.put(new LayoutKey(false, false, true),  Layout.CUSTOM_UPDATE);
            MAP.put(new LayoutKey(false, true,  false), Layout.CUSTOM_FORECASTS);
            MAP.put(new LayoutKey(false, true,  true),  Layout.CUSTOM_FORECASTS_UPDATE);
            MAP.put(new LayoutKey(true,  false, false), Layout.CUSTOM_ICON);
            MAP.put(new LayoutKey(true,  false, true),  Layout.CUSTOM_ICON_UPDATE);
            MAP.put(new LayoutKey(true,  true,  false), Layout.CUSTOM_ICON_FORECASTS);
            MAP.put(new LayoutKey(true,  true,  true),  Layout.CUSTOM_ICON_FORECASTS_UPDATE);
        }

        public static Layout get(boolean showIcon, boolean showForecasts, boolean showUpdateTime) {
            LayoutKey key = new LayoutKey(showIcon, showForecasts, showUpdateTime);
            return MAP.get(key);
        }
    }

    private final Context context;

    private final TemperatureType tempType;
    private final NotificationStyle notifyStyle;
    private final NotificationTextStyle textStyle;
    private final boolean showIcon;
    private final boolean showForecasts;

    public NotificationStyler(Context context) {
        this.context = context;

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);

        this.tempType = TemperatureType.valueOf(prefs.getString(
                TEMP_UNIT, TEMP_UNIT_DEFAULT));
        this.notifyStyle = NotificationStyle.valueOf(prefs.getString(
                NOTIFICATION_STYLE, NOTIFICATION_STYLE_DEFAULT));
        this.textStyle = NotificationTextStyle.valueOf(prefs.getString(
                NOTIFICATION_TEXT_STYLE, NOTIFICATION_TEXT_STYLE_DEFAULT));
        this.showIcon = prefs.getBoolean(
                NOTIFICATION_ICON_STYLE, NOTIFICATION_ICON_STYLE_DEFAULT);
        this.showForecasts = prefs.getBoolean(
                NOTIFICATION_FORECASTS_STYLE, NOTIFICATION_FORECASTS_STYLE_DEFAULT);
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

    public boolean isShowForecasts() {
        return showForecasts;
    }

    /**
     *  Returns the notification layout id.
     */
    protected int getLayoutId() {
        ResourceIdFactory ids = ResourceIdFactory.getInstance(this.context);
        Layout layout = Layout.get(isShowIcon(), isShowForecasts(), isShowUpdateTime());
        if (layout != null) {
            return ids.id(ResourceIdFactory.LAYOUT, layout.name);
        }
        return 0;   //unknown resource
    }

    private boolean isShowUpdateTime() {
        switch (getTempType()) {
            case C:
            case F:
            default:
                return true;
            case CF:
            case FC:
                return false;
        }
    }

}
