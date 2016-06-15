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

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceGroup;
import android.preference.PreferenceManager;
import ru.gelin.android.weather.notification.skin.UpdateNotificationActivity;

import static ru.gelin.android.weather.notification.skin.impl.ResourceIdFactory.XML;

/**
 *  Base class for skin configuration activity.
 */
public class BaseConfigActivity extends UpdateNotificationActivity 
        implements OnPreferenceChangeListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ResourceIdFactory ids = ResourceIdFactory.getInstance(this);

        addPreferencesFromResource(ids.id(XML, "skin_preferences"));
        enablePreferences(getValue(PreferenceKeys.NOTIFICATION_STYLE, PreferenceKeys.NOTIFICATION_STYLE_DEFAULT));
        
        addPreferenceListener(getPreferenceScreen());
    }

    private void addPreferenceListener(PreferenceGroup prefs) {
        for (int i = 0; i < prefs.getPreferenceCount(); i++) {
            Preference pref = prefs.getPreference(i);
            if (pref instanceof PreferenceGroup) {
                addPreferenceListener((PreferenceGroup) pref);
            } else {
                pref.setOnPreferenceChangeListener(this);
            }
        }
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        String key = preference.getKey();
        if (PreferenceKeys.NOTIFICATION_STYLE.equals(key)) {
            enablePreferences(newValue);
        } else if (PreferenceKeys.NOTIFICATION_TEXT_STYLE.equals(key)) {
            checkBackgroundStyle(newValue);
        } else if (PreferenceKeys.NOTIFICATION_BACK_STYLE.equals(key)) {
            checkTextStyle(newValue);
        }
        updateNotification();
        return true;
    }

    private void enablePreferences(Object notificationStyleValue) {
        boolean enabled = NotificationStyle.CUSTOM_STYLE.equals(
                NotificationStyle.valueOf(String.valueOf(notificationStyleValue)));
        findPreference(PreferenceKeys.NOTIFICATION_TEXT_STYLE).setEnabled(enabled);
        findPreference(PreferenceKeys.NOTIFICATION_BACK_STYLE).setEnabled(enabled);
        findPreference(PreferenceKeys.NOTIFICATION_ICON_STYLE).setEnabled(enabled);
        findPreference(PreferenceKeys.NOTIFICATION_FORECASTS_STYLE).setEnabled(enabled);
    }

    private void checkBackgroundStyle(Object textStyleValue) {
        NotificationTextStyle textStyle = NotificationTextStyle.valueOf(String.valueOf(textStyleValue));
        NotificationBackStyle backStyle = NotificationBackStyle.valueOf(
                getValue(PreferenceKeys.NOTIFICATION_BACK_STYLE, PreferenceKeys.NOTIFICATION_BACK_STYLE_DEFAULT));
        switch (textStyle) {
            case BLACK_TEXT:
                if (NotificationBackStyle.BLACK_BACK.equals(backStyle)) {
                    setValue(PreferenceKeys.NOTIFICATION_BACK_STYLE, NotificationBackStyle.DEFAULT_BACK.toString());
                }
                break;
            case WHITE_TEXT:
                if (NotificationBackStyle.WHITE_BACK.equals(backStyle)) {
                    setValue(PreferenceKeys.NOTIFICATION_BACK_STYLE, NotificationBackStyle.DEFAULT_BACK.toString());
                }
        }
    }

    private void checkTextStyle(Object backStyleValue) {
        NotificationBackStyle backStyle = NotificationBackStyle.valueOf(String.valueOf(backStyleValue));
        NotificationTextStyle textStyle = NotificationTextStyle.valueOf(
                getValue(PreferenceKeys.NOTIFICATION_TEXT_STYLE, PreferenceKeys.NOTIFICATION_TEXT_STYLE_DEFAULT));
        switch (backStyle) {
            case BLACK_BACK:
                if (NotificationTextStyle.BLACK_TEXT.equals(textStyle)) {
                    setValue(PreferenceKeys.NOTIFICATION_TEXT_STYLE, NotificationTextStyle.WHITE_TEXT.toString());
                }
                break;
            case WHITE_BACK:
                if (NotificationTextStyle.WHITE_TEXT.equals(textStyle)) {
                    setValue(PreferenceKeys.NOTIFICATION_TEXT_STYLE, NotificationTextStyle.BLACK_TEXT.toString());
                }
        }
    }

    private String getValue(String prefKey, String defaultValue) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        return prefs.getString(prefKey, defaultValue);
    }

    private void setValue(String prefKey, String value) {
        ListPreference pref = (ListPreference)findPreference(prefKey);
        pref.setValue(value);
        pref.setSummary(pref.getEntry());
    }

}